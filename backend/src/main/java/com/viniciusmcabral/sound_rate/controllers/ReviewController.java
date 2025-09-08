package com.viniciusmcabral.sound_rate.controllers;

import com.viniciusmcabral.sound_rate.dtos.request.ReviewRequestDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@PostMapping
	public ResponseEntity<AlbumReviewDTO> createReview(@RequestBody @Valid ReviewRequestDTO reviewDto) {
		AlbumReviewDTO newReview = reviewService.createReview(reviewDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(newReview);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AlbumReviewDTO> updateReview(@PathVariable Long id, @RequestBody @Valid ReviewRequestDTO reviewDto) {
		AlbumReviewDTO updatedReview = reviewService.updateReview(id, reviewDto);
		return ResponseEntity.ok(updatedReview);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
		reviewService.deleteReview(id);
		return ResponseEntity.noContent().build();
	}
}