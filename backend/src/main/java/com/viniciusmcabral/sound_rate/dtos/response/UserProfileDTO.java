package com.viniciusmcabral.sound_rate.dtos.response;

public record UserProfileDTO(UserDTO user, long totalReviews, long totalAlbumRatings, long totalTrackRatings, long followersCount, long followingCount, boolean isFollowedByCurrentUser) {
}
