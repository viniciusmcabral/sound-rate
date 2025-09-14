package com.viniciusmcabral.sound_rate.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.viniciusmcabral.sound_rate.dtos.request.RegisterRequestDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AuthResponseDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.UserRepository;

@Service
public class AuthService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByLoginAndActiveTrue(username).orElseThrow(() -> new UsernameNotFoundException("User not found or is inactive with identifier: " + username));
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

		String token = tokenService.generateToken(newUser);
		UserDTO userDTO = new UserDTO(newUser.getId(), newUser.getUsername(), newUser.getAvatarUrl());

		return new AuthResponseDTO(token, userDTO);
	}
}