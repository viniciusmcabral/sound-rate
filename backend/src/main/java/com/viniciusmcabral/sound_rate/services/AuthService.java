package com.viniciusmcabral.sound_rate.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viniciusmcabral.sound_rate.dtos.request.RegisterUserDTO;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.UserRepository;

@Service
public class AuthService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		return userRepository.findByUsernameOrEmail(login, login)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + login));
	}
	
	public User registerUser(RegisterUserDTO registerDTO) {
        if (userRepository.findByUsername(registerDTO.username()).isPresent()) {
            throw new IllegalStateException("Username already taken");
        }

        if (userRepository.findByEmail(registerDTO.email()).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(registerDTO.password());
        User newUser = new User(registerDTO.username(), registerDTO.email(), hashedPassword);

        return userRepository.save(newUser);
    }
}
