package com.viniciusmcabral.sound_rate.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmcabral.sound_rate.dtos.response.AlbumDashboardDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumDetailsDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.services.AlbumService;
import com.viniciusmcabral.sound_rate.services.ReviewService;

@RestController
@RequestMapping("/albums")
public class AlbumController {

	private final AlbumService albumService;
	private final ReviewService reviewService;

	public AlbumController(AlbumService albumService, ReviewService reviewService) {
		this.albumService = albumService;
		this.reviewService = reviewService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlbumDetailsDTO> getAlbumById(@PathVariable String id) {
		AlbumDetailsDTO albumDetails = albumService.getAlbumDetails(id);
		return ResponseEntity.ok(albumDetails);
	}

	@GetMapping("/{albumId}/reviews")
	public ResponseEntity<Page<AlbumReviewDTO>> getReviewsForAlbum(@PathVariable String albumId, Pageable pageable) {
		Page<AlbumReviewDTO> reviews = reviewService.getReviewsForAlbum(albumId, pageable);
		return ResponseEntity.ok(reviews);
	}

	@GetMapping("/highest-rated")
	public ResponseEntity<List<AlbumDashboardDTO>> getHighestRatedAlbums() {
		List<AlbumDashboardDTO> albums = albumService.getHighestRatedAlbums();
		return ResponseEntity.ok(albums);
	}
}