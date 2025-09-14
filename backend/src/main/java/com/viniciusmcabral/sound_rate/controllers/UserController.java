package com.viniciusmcabral.sound_rate.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import com.viniciusmcabral.sound_rate.dtos.request.UpdatePasswordDTO;
import com.viniciusmcabral.sound_rate.dtos.request.UpdateProfileDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserProfileDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserRatingDTO;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.services.UserService;

import jakarta.validation.Valid;

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

	@GetMapping("/{username}/likes")
	public ResponseEntity<Page<DeezerAlbumDTO>> getLikedAlbums(@PathVariable String username,
			@PageableDefault(size = 20) Pageable pageable) {
		Page<DeezerAlbumDTO> likedAlbumsPage = userService.getLikedAlbumsPage(username, pageable);
		return ResponseEntity.ok(likedAlbumsPage);
	}

	@GetMapping("/{username}/ratings")
	public ResponseEntity<Page<UserRatingDTO>> getRatedAlbums(@PathVariable String username,
			@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<UserRatingDTO> ratedAlbumsPage = userService.getRatedAlbumsPage(username, pageable);
		return ResponseEntity.ok(ratedAlbumsPage);
	}

	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteCurrentUser() {
		userService.deleteCurrentUser();
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/me/profile")
	public ResponseEntity<UserDTO> updateProfile(@AuthenticationPrincipal User currentUser,
			@RequestBody @Valid UpdateProfileDTO data) {
		UserDTO updatedUser = userService.updateProfile(currentUser, data);
		return ResponseEntity.ok(updatedUser);
	}

	@PutMapping("/me/password")
	public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal User currentUser,
			@RequestBody @Valid UpdatePasswordDTO data) {
		userService.updatePassword(currentUser, data);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/me/avatar")
	public ResponseEntity<UserDTO> updateAvatar(@AuthenticationPrincipal User currentUser,
			@RequestParam("file") MultipartFile file) {
		UserDTO updatedUser = userService.updateAvatar(currentUser, file);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/me/avatar")
	public ResponseEntity<UserDTO> resetAvatar(@AuthenticationPrincipal User currentUser) {
		UserDTO updatedUser = userService.resetAvatar(currentUser);
		return ResponseEntity.ok(updatedUser);
	}
}