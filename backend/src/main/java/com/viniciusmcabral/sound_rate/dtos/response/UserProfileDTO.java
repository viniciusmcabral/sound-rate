package com.viniciusmcabral.sound_rate.dtos.response;

import java.util.List;

public record UserProfileDTO(UserDTO user, long totalReviews, long totalAlbumRatings, long totalTrackRatings,
		List<AlbumReviewDTO> recentReviews, List<AlbumRatingDTO> recentAlbumRatings,
		List<TrackRatingDTO> recentTrackRatings) {

}
