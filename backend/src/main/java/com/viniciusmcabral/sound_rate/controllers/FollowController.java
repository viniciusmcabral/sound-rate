package com.viniciusmcabral.sound_rate.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.services.FollowService;

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

	@GetMapping("/{username}/followers")
	public ResponseEntity<Page<UserDTO>> getFollowers(@PathVariable String username, @PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(followService.getFollowers(username, pageable));
	}

	@GetMapping("/{username}/following")
	public ResponseEntity<Page<UserDTO>> getFollowing(@PathVariable String username, @PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(followService.getFollowing(username, pageable));
	}
}