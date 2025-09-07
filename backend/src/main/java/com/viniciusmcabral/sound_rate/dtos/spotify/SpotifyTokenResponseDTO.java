package com.viniciusmcabral.sound_rate.dtos.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyTokenResponseDTO(@JsonProperty("access_token") String accessToken) {

}
