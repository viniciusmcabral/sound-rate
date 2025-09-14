package com.viniciusmcabral.sound_rate.dtos.response;

import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import java.util.List;

public record AlbumDetailsDTO(DeezerAlbumDTO deezerDetails, Double communityScore, Double currentUserRating,
		AlbumReviewDTO currentUserReview, List<AlbumReviewDTO> userReviews, long likesCount,
		boolean isLikedByCurrentUser, boolean isOnListenLaterList) {
}