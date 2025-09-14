package com.viniciusmcabral.sound_rate.services;

import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerSearchResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Collections;
import java.util.List;

@Service
public class DeezerService {

	private static final Logger logger = LoggerFactory.getLogger(DeezerService.class);
	private final RestTemplate restTemplate;
	private final String deezerApiUrl = "https://api.deezer.com";

	public DeezerService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<DeezerAlbumDTO> searchAlbums(String query) {
		String url = UriComponentsBuilder.fromHttpUrl(deezerApiUrl + "/search/album").queryParam("q", query).queryParam("limit", 20).toUriString();

		logger.info("Searching Deezer albums with URL: {}", url);

		try {
			DeezerSearchResponseDTO response = restTemplate.getForObject(url, DeezerSearchResponseDTO.class);
			return response != null ? response.data() : Collections.emptyList();
		} catch (RestClientException e) {
			logger.error("Error searching albums on Deezer for query '{}': {}", query, e.getMessage());
			return Collections.emptyList();
		}
	}

	public DeezerAlbumDTO getAlbumDetails(String albumId) {
		String url = deezerApiUrl + "/album/" + albumId;
		logger.info("Fetching Deezer album details from URL: {}", url);

		try {
			return restTemplate.getForObject(url, DeezerAlbumDTO.class);
		} catch (RestClientException e) {
			logger.error("Error fetching album details from Deezer for ID {}: {}", albumId, e.getMessage());
			return null;
		}
	}
}