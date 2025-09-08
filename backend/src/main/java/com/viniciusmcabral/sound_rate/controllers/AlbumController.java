package com.viniciusmcabral.sound_rate.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmcabral.sound_rate.dtos.response.AlbumDetailsDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.dtos.spotify.SpotifyAlbumDTO;
import com.viniciusmcabral.sound_rate.services.AlbumService;
import com.viniciusmcabral.sound_rate.services.ReviewService;
import com.viniciusmcabral.sound_rate.services.SpotifyService;

@RestController
@RequestMapping("/albums")
public class AlbumController {

	private final AlbumService albumService;
	private final SpotifyService spotifyService;
	private final ReviewService reviewService;

	public AlbumController(AlbumService albumService, SpotifyService spotifyService, ReviewService reviewService) {
		this.albumService = albumService;
		this.spotifyService = spotifyService;
		this.reviewService = reviewService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlbumDetailsDTO> getAlbumById(@PathVariable String id) {
		AlbumDetailsDTO albumDetails = albumService.getAlbumDetails(id);
		return ResponseEntity.ok(albumDetails);
	}

	@GetMapping("/search")
	public ResponseEntity<List<SpotifyAlbumDTO>> searchAlbums(@RequestParam("query") String query) {
		List<SpotifyAlbumDTO> results = spotifyService.searchAlbums(query);
		return ResponseEntity.ok(results);
	}

	@GetMapping("/{albumId}/reviews")
	public ResponseEntity<List<AlbumReviewDTO>> getReviewsForAlbum(@PathVariable String albumId) {
		List<AlbumReviewDTO> reviews = reviewService.getReviewsForAlbum(albumId);
		return ResponseEntity.ok(reviews);
	}
}