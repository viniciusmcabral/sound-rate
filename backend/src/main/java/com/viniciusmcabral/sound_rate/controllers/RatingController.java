package com.viniciusmcabral.sound_rate.controllers;

import com.viniciusmcabral.sound_rate.dtos.request.RatingRequestDTO;
import com.viniciusmcabral.sound_rate.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ratings")
public class RatingController {

	private final RatingService ratingService;

	public RatingController(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	@PostMapping
	public ResponseEntity<Void> createOrUpdateRating(@RequestBody @Valid RatingRequestDTO ratingDto) {
		ratingService.rateAlbumOrTrack(ratingDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getCurrentUserRatings() {
		Map<String, Object> userRatings = ratingService.getUserRatings();
		return ResponseEntity.ok(userRatings);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteRating(@RequestParam(required = false) String albumId, @RequestParam(required = false) String trackId) {
		ratingService.deleteRating(albumId, trackId);
		return ResponseEntity.noContent().build();
	}
}