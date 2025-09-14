package com.viniciusmcabral.sound_rate.dtos.response;

import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import java.time.LocalDateTime;

public record UserRatingDTO(DeezerAlbumDTO album, Double rating, LocalDateTime ratingDate, String reviewText) {
}