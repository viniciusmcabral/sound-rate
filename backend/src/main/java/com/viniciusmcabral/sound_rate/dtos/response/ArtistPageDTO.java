package com.viniciusmcabral.sound_rate.dtos.response;

import org.springframework.data.domain.Page;

import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerArtistDetailsDTO;

public record ArtistPageDTO(DeezerArtistDetailsDTO artistDetails, Page<DeezerAlbumDTO> albums) {
}