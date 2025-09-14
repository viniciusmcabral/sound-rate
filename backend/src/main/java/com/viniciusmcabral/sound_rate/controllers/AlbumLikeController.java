package com.viniciusmcabral.sound_rate.controllers;

import com.viniciusmcabral.sound_rate.services.AlbumLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")
public class AlbumLikeController {

	private final AlbumLikeService albumLikeService;

	public AlbumLikeController(AlbumLikeService albumLikeService) {
		this.albumLikeService = albumLikeService;
	}

	@PostMapping("/{albumId}/like")
	public ResponseEntity<Void> likeAlbum(@PathVariable String albumId) {
		albumLikeService.likeAlbum(albumId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{albumId}/like")
	public ResponseEntity<Void> unlikeAlbum(@PathVariable String albumId) {
		albumLikeService.unlikeAlbum(albumId);
		return ResponseEntity.noContent().build();
	}
}