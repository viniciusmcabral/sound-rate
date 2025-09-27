package com.viniciusmcabral.sound_rate.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmcabral.sound_rate.dtos.response.SearchResultDTO;
import com.viniciusmcabral.sound_rate.services.SearchService;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

	private final SearchService searchService;

	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

	@GetMapping
	public ResponseEntity<List<SearchResultDTO>> search(@RequestParam("query") String query) {
		List<SearchResultDTO> results = searchService.searchAll(query);
		return ResponseEntity.ok(results);
	}
}