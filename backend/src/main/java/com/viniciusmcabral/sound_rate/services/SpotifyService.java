package com.viniciusmcabral.sound_rate.services;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.viniciusmcabral.sound_rate.dtos.spotify.SpotifyAlbumDTO;
import com.viniciusmcabral.sound_rate.dtos.spotify.SpotifySearchResponseDTO;
import com.viniciusmcabral.sound_rate.dtos.spotify.SpotifyTokenResponseDTO;

@Service
public class SpotifyService {
	
	private RestTemplate restTemplate;

	@Value("${spotify.client.id}")
	private String spotifyClientId;

	@Value("${spotify.client.secret}")
	private String spotifyClientSecret;

	private String spotifyAccessToken;

	public SpotifyService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<SpotifyAlbumDTO> searchAlbums(String query) {
		ensureToken();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + this.spotifyAccessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		String url = UriComponentsBuilder.fromHttpUrl("https://api.spotify.com/v1/search?q=").queryParam("q", query)
				.queryParam("type", "album").queryParam("limit", 20).toUriString();

		try {
			ResponseEntity<SpotifySearchResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity,SpotifySearchResponseDTO.class);
			return response.getBody() != null ? response.getBody().albums().items() : Collections.emptyList();
		} catch (RestClientException e) {
			return Collections.emptyList(); 
		}
	}

	public SpotifyAlbumDTO getAlbumDetails(String albumId) {
		ensureToken();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + this.spotifyAccessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		String url = "https://api.spotify.com/v1/albums/" + albumId;

		try {
			ResponseEntity<SpotifyAlbumDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, SpotifyAlbumDTO.class);
			return response.getBody();
		} catch (RestClientException e) {
			return null; 
		}
	}

	private void ensureToken() {
		authenticateWithSpotify();
	}

	private void authenticateWithSpotify() {
		String authUrl = "https://accounts.spotify.com/api/token";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String authHeader = Base64.getEncoder().encodeToString((spotifyClientId + ":" + spotifyClientSecret).getBytes());
		headers.set("Authorization", "Basic " + authHeader);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "client_credentials");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

		try {
			ResponseEntity<SpotifyTokenResponseDTO> response = restTemplate.postForEntity(authUrl, request,SpotifyTokenResponseDTO.class);
			if (response.getBody() != null) {
				this.spotifyAccessToken = response.getBody().accessToken();
			}
		} catch (RestClientException e) {
			throw new RuntimeException("Failed to authenticate with Spotify API", e);
		}
	}
}