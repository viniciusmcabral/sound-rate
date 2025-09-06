package com.viniciusmcabral.sound_rate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmcabral.sound_rate.dtos.request.LoginRequestDTO;
import com.viniciusmcabral.sound_rate.dtos.request.RegisterUserDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AuthResponseDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.services.AuthService;
import com.viniciusmcabral.sound_rate.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationManager manager;
	private final TokenService tokenService;
	private final AuthService authService;

	public AuthController(AuthenticationManager manager, TokenService tokenService, AuthService authService) {
		this.manager = manager;
		this.tokenService = tokenService;
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginData) {
		var authToken = new UsernamePasswordAuthenticationToken(loginData.username(), loginData.password());
		var authentication = manager.authenticate(authToken);
		var user = (User) authentication.getPrincipal();
		var tokenJWT = tokenService.generateToken(user);
		var userDto = new UserDTO(user.getId(), user.getUsername());
		
		return ResponseEntity.ok(new AuthResponseDTO(tokenJWT, userDto));
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterUserDTO registerDTO) {
		User newUser = authService.registerUser(registerDTO);
		var tokenJWT = tokenService.generateToken(newUser);
		var userDto = new UserDTO(newUser.getId(), newUser.getUsername());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDTO(tokenJWT, userDto));
	}
}