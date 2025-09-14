package com.viniciusmcabral.sound_rate.controllers;

import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.services.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class FollowController {

	private final FollowService followService;

	public FollowController(FollowService followService) {
		this.followService = followService;
	}

	@PostMapping("/{username}/follow")
	public ResponseEntity<Void> followUser(@PathVariable String username, @AuthenticationPrincipal User currentUser) {
		followService.followUser(username, currentUser);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{username}/follow")
	public ResponseEntity<Void> unfollowUser(@PathVariable String username, @AuthenticationPrincipal User currentUser) {
		followService.unfollowUser(username, currentUser);
		return ResponseEntity.noContent().build();
	}
}