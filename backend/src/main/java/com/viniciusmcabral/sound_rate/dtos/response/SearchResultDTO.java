package com.viniciusmcabral.sound_rate.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerArtistDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchResultDTO(String type, DeezerAlbumDTO album, DeezerArtistDTO artist, UserDTO user) {
}