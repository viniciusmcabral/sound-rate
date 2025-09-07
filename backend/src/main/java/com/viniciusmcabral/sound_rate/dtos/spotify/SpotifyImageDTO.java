package com.viniciusmcabral.sound_rate.dtos.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyImageDTO(String url, int height, int width) {

}
