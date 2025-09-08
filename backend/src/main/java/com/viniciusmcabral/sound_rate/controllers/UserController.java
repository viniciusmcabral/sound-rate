package com.viniciusmcabral.sound_rate.controllers;

import com.viniciusmcabral.sound_rate.dtos.response.UserProfileDTO;
import com.viniciusmcabral.sound_rate.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{username}")
	public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String username) {
		UserProfileDTO userProfile = userService.getUserProfile(username);
		return ResponseEntity.ok(userProfile);
	}
}