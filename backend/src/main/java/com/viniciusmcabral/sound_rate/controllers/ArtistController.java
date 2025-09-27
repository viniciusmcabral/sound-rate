package com.viniciusmcabral.sound_rate.controllers;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmcabral.sound_rate.dtos.response.ArtistPageDTO;
import com.viniciusmcabral.sound_rate.services.ArtistService;

@RestController
@RequestMapping("/api/v1/artists")
public class ArtistController {

	private final ArtistService artistService;

	public ArtistController(ArtistService artistService) {
		this.artistService = artistService;
	}

	@GetMapping("/{artistId}")
	public ResponseEntity<ArtistPageDTO> getArtistPage(@PathVariable String artistId, Pageable pageable) {
		try {
			ArtistPageDTO response = artistService.getArtistPageDetails(artistId, pageable);
			return ResponseEntity.ok(response);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}
}