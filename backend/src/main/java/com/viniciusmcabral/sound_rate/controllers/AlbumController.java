package com.viniciusmcabral.sound_rate.controllers;

import com.viniciusmcabral.sound_rate.dtos.response.AlbumDetailsDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import com.viniciusmcabral.sound_rate.services.AlbumService;
import com.viniciusmcabral.sound_rate.services.ReviewService;
import com.viniciusmcabral.sound_rate.services.DeezerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

	private final AlbumService albumService;
	private final DeezerService deezerService;
	private final ReviewService reviewService;

	public AlbumController(AlbumService albumService, DeezerService deezerService, ReviewService reviewService) {
		this.albumService = albumService;
		this.deezerService = deezerService;
		this.reviewService = reviewService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlbumDetailsDTO> getAlbumById(@PathVariable String id) {
		AlbumDetailsDTO albumDetails = albumService.getAlbumDetails(id);
		return ResponseEntity.ok(albumDetails);
	}

	@GetMapping("/search")
	public ResponseEntity<List<DeezerAlbumDTO>> searchAlbums(@RequestParam("query") String query) {
		List<DeezerAlbumDTO> results = deezerService.searchAlbums(query);
		return ResponseEntity.ok(results);
	}

	@GetMapping("/{albumId}/reviews")
	public ResponseEntity<Page<AlbumReviewDTO>> getReviewsForAlbum(@PathVariable String albumId, Pageable pageable) {
		Page<AlbumReviewDTO> reviews = reviewService.getReviewsForAlbum(albumId, pageable);
		return ResponseEntity.ok(reviews);
	}
}
