package com.viniciusmcabral.sound_rate.dtos.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyArtistDTO(String id, String name) {

}
