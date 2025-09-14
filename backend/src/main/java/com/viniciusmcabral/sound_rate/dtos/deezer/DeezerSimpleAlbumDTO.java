package com.viniciusmcabral.sound_rate.dtos.deezer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeezerSimpleAlbumDTO(long id, String title, String cover,
		@JsonProperty("cover_medium") String coverMedium) {
}