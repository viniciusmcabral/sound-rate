package com.viniciusmcabral.sound_rate.dtos.spotify;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyAlbumDTO(String id, String name, List<SpotifyArtistDTO> artists, List<SpotifyImageDTO> images) {

}
