package com.viniciusmcabral.sound_rate.dtos.deezer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeezerArtistSearchResponseDTO(List<DeezerArtistDTO> data) {
}