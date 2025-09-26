package com.viniciusmcabral.sound_rate.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusmcabral.sound_rate.dtos.request.RegisterRequestDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AuthResponseDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.models.PasswordResetToken;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.PasswordResetTokenRepository;
import com.viniciusmcabral.sound_rate.repositories.UserRepository;

@Service
public class AuthService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;
	private final PasswordResetTokenRepository tokenRepository;
	private final EmailService emailService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService,
			PasswordResetTokenRepository tokenRepository, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
		this.tokenRepository = tokenRepository;
		this.emailService = emailService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByLoginAndActiveTrue(username).orElseThrow(
				() -> new UsernameNotFoundException("User not found or is inactive with identifier: " + username));
	}

	@Transactional
	public AuthResponseDTO registerUser(RegisterRequestDTO data) {
		if (userRepository.findByUsername(data.username()).isPresent())
			throw new IllegalStateException("Username already exists");

		if (userRepository.findByEmail(data.email()).isPresent())
			throw new IllegalStateException("Email already in use");

		User newUser = new User(data.username(), data.email(), passwordEncoder.encode(data.password()));

		String avatarUrl = "https://api.dicebear.com/8.x/initials/svg?seed=" + newUser.getUsername();
		newUser.setAvatarUrl(avatarUrl);

		userRepository.save(newUser);
		emailService.sendWelcomeEmail(newUser.getEmail(), newUser.getUsername());

		String token = tokenService.generateToken(newUser);
		UserDTO userDTO = new UserDTO(newUser.getId(), newUser.getUsername(), newUser.getAvatarUrl());

		return new AuthResponseDTO(token, userDTO);
	}

	@Transactional
	public void requestPasswordReset(String userEmail) {
		Optional<User> userOpt = userRepository.findByEmail(userEmail);

		if (userOpt.isEmpty()) {
			return;
		}

		User user = userOpt.get();
		tokenRepository.deleteByUser(user);

		String tokenString = UUID.randomUUID().toString();
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setToken(tokenString);
		passwordResetToken.setUser(user);
		passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
		tokenRepository.save(passwordResetToken);

		String resetLink = "http://localhost:4200/reset-password?token=" + tokenString;
		emailService.sendPasswordResetEmail(user.getEmail(), user.getUsername(), resetLink);
	}

	@Transactional
	public void performPasswordReset(String token, String newPassword) {
		PasswordResetToken resetToken = tokenRepository.findByToken(token)
				.orElseThrow(() -> new IllegalArgumentException("Invalid password reset token."));

		if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			tokenRepository.delete(resetToken);
			throw new IllegalArgumentException("Password reset token has expired.");
		}

		User user = resetToken.getUser();
		String encodedPassword = passwordEncoder.encode(newPassword);

		user.setPassword(encodedPassword);
		userRepository.save(user);
		tokenRepository.delete(resetToken);
	}
}