package com.viniciusmcabral.sound_rate.services;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumSearchResponseDTO;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerArtistAlbumsResponseDTO;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerArtistDTO;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerArtistDetailsDTO;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerArtistSearchResponseDTO;

@Service
public class DeezerService {

	private static final Logger logger = LoggerFactory.getLogger(DeezerService.class);
	private final RestTemplate restTemplate;
	private final String deezerApiUrl = "https://api.deezer.com";

	public DeezerService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<DeezerAlbumDTO> searchAlbums(String query) {
		String url = UriComponentsBuilder.fromHttpUrl(deezerApiUrl + "/search/album").queryParam("q", query)
				.toUriString();

		logger.info("Searching Deezer albums with URL: {}", url);

		try {
			DeezerAlbumSearchResponseDTO response = restTemplate.getForObject(url, DeezerAlbumSearchResponseDTO.class);
			return response != null ? response.data() : Collections.emptyList();
		} catch (RestClientException e) {
			logger.error("Error searching albums on Deezer for query '{}': {}", query, e.getMessage());
			return Collections.emptyList();
		}
	}

	public List<DeezerArtistDTO> searchArtists(String query) {
		String url = UriComponentsBuilder.fromHttpUrl(deezerApiUrl + "/search/artist").queryParam("q", query)
				.toUriString();

		try {
			DeezerArtistSearchResponseDTO response = restTemplate.getForObject(url,
					DeezerArtistSearchResponseDTO.class);
			return response != null ? response.data() : Collections.emptyList();
		} catch (RestClientException e) {
			logger.error("Error searching artists on Deezer for query '{}': {}", query, e.getMessage());
			return Collections.emptyList();
		}
	}

	public DeezerArtistDetailsDTO getArtistDetails(String artistId) {
		String url = deezerApiUrl + "/artist/" + artistId;
		try {
			return restTemplate.getForObject(url, DeezerArtistDetailsDTO.class);
		} catch (RestClientException e) {
			logger.error("Error fetching artist details from Deezer for ID {}: {}", artistId, e.getMessage());
			return null;
		}
	}

	public Page<DeezerAlbumDTO> getArtistAlbums(String artistId, Pageable pageable) {
		int index = pageable.getPageNumber() * pageable.getPageSize();
		int limit = pageable.getPageSize();

		String url = UriComponentsBuilder.fromHttpUrl(deezerApiUrl + "/artist/" + artistId + "/albums")
				.queryParam("index", index).queryParam("limit", limit).toUriString();

		try {
			DeezerArtistAlbumsResponseDTO response = restTemplate.getForObject(url,
					DeezerArtistAlbumsResponseDTO.class);
			if (response != null) {
				return new PageImpl<>(response.data(), pageable, response.total());
			}
		} catch (RestClientException e) {
			logger.error("Error fetching paginated albums for artist {}: {}", artistId, e.getMessage());
		}

		return Page.empty(pageable);
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