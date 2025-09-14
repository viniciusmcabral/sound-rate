package com.viniciusmcabral.sound_rate.dtos.deezer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeezerArtistDTO(long id, String name, String link, String picture,
		@JsonProperty("picture_medium") String pictureMedium, String tracklist) {
}