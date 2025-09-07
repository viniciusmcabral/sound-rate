package com.viniciusmcabral.sound_rate.dtos.response;

import java.util.List;

import com.viniciusmcabral.sound_rate.dtos.spotify.SpotifyAlbumDTO;

public record AlbumDetailsDTO(SpotifyAlbumDTO spotifyDetails, CriticReviewDTO criticReview, Double communityScore, Integer currentUserRating, List<AlbumReviewDTO> userReviews) {

}
