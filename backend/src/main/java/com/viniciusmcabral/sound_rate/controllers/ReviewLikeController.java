package com.viniciusmcabral.sound_rate.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmcabral.sound_rate.services.ReviewLikeService;

@RestController
@RequestMapping("/reviews")
public class ReviewLikeController {

	private final ReviewLikeService reviewLikeService;

	public ReviewLikeController(ReviewLikeService reviewLikeService) {
		this.reviewLikeService = reviewLikeService;
	}

	@PostMapping("/{reviewId}/like")
	public ResponseEntity<Void> likeReview(@PathVariable Long reviewId) {
		reviewLikeService.likeReview(reviewId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{reviewId}/like")
	public ResponseEntity<Void> unlikeReview(@PathVariable Long reviewId) {
		reviewLikeService.unlikeReview(reviewId);
		return ResponseEntity.noContent().build();
	}
}