package com.viniciusmcabral.sound_rate.services;

import com.viniciusmcabral.sound_rate.dtos.response.SearchResultDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

@Service
public class SearchService {

	private final DeezerService deezerService;
	private final UserService userService;

	public SearchService(DeezerService deezerService, UserService userService) {
		this.deezerService = deezerService;
		this.userService = userService;
	}

	public List<SearchResultDTO> searchAll(String query) {
		var albums = deezerService.searchAlbums(query).stream().map(album -> new SearchResultDTO("album", album, null, null));
		var artists = deezerService.searchArtists(query).stream().map(artist -> new SearchResultDTO("artist", null, artist, null));
		var users = userService.searchUsers(query, PageRequest.of(0, 5)).getContent().stream().map(user -> new SearchResultDTO("user", null, null, user));

		return Stream.concat(Stream.concat(albums, artists), users).collect(Collectors.toList());
	}
}