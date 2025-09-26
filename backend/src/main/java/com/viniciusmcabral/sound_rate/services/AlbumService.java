package com.viniciusmcabral.sound_rate.services;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumDashboardDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumDetailsDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.dtos.response.TrackRatingDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.models.AlbumRating;
import com.viniciusmcabral.sound_rate.models.AlbumReview;
import com.viniciusmcabral.sound_rate.models.TrackRating;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumLikeRepository;
import com.viniciusmcabral.sound_rate.repositories.AlbumRatingRepository;
import com.viniciusmcabral.sound_rate.repositories.AlbumReviewRepository;
import com.viniciusmcabral.sound_rate.repositories.ListenLaterRepository;
import com.viniciusmcabral.sound_rate.repositories.ReviewLikeRepository;
import com.viniciusmcabral.sound_rate.repositories.TrackRatingRepository;

@Service
public class AlbumService {

	private final DeezerService deezerService;
	private final AlbumRatingRepository albumRatingRepository;
	private final AlbumReviewRepository albumReviewRepository;
	private final AlbumLikeRepository albumLikeRepository;
	private final ReviewLikeRepository reviewLikeRepository;
	private final ListenLaterRepository listenLaterRepository;
	private final TrackRatingRepository trackRatingRepository;

	public AlbumService(DeezerService deezerService, AlbumRatingRepository albumRatingRepository,
			AlbumReviewRepository albumReviewRepository, AlbumLikeRepository albumLikeRepository,
			ReviewLikeRepository reviewLikeRepository, ListenLaterRepository listenLaterRepository,
			TrackRatingRepository trackRatingRepository) {
		this.deezerService = deezerService;
		this.albumRatingRepository = albumRatingRepository;
		this.albumReviewRepository = albumReviewRepository;
		this.albumLikeRepository = albumLikeRepository;
		this.reviewLikeRepository = reviewLikeRepository;
		this.listenLaterRepository = listenLaterRepository;
		this.trackRatingRepository = trackRatingRepository;
	}

	@Transactional(readOnly = true)
	public AlbumDetailsDTO getAlbumDetails(String albumId) {
		User currentUser = getCurrentUserOrNull();

		if (currentUser == null)
			return null;

		DeezerAlbumDTO deezerDetails = Optional.ofNullable(deezerService.getAlbumDetails(albumId))
				.orElseThrow(() -> new NoSuchElementException("Album not found on Deezer with ID: " + albumId));

		Double communityScore = albumRatingRepository.findCommunityAverageRating(albumId).orElse(null);

		Pageable firstPageOfReviews = PageRequest.of(0, 10, Sort.by("createdAt").descending());

		List<AlbumReviewDTO> userReviews = albumReviewRepository.findActiveReviewsByAlbumId(albumId, firstPageOfReviews)
				.getContent().stream().map(this::convertReviewToDto).collect(Collectors.toList());
		List<TrackRatingDTO> currentUserTrackRatings = trackRatingRepository.findByUserAndAlbumId(currentUser, albumId)
				.stream().map(this::convertTrackRatingToDto).collect(Collectors.toList());

		Double currentUserRating = findCurrentUserRating(albumId);
		AlbumReviewDTO currentUserReview = findCurrentUserReview(albumId);

		long likesCount = albumLikeRepository.countByAlbumId(albumId);
		boolean isLikedByCurrentUser = isAlbumLikedByCurrentUser(albumId);
		boolean isOnListenLaterList = isAlbumOnListenLaterList(albumId);
		long ratingsCount = albumRatingRepository.countByAlbumId(albumId);

		return new AlbumDetailsDTO(deezerDetails, communityScore, currentUserRating, currentUserReview, userReviews,
				likesCount, isLikedByCurrentUser, isOnListenLaterList, currentUserTrackRatings, ratingsCount);
	}

	@Transactional(readOnly = true)
	public List<AlbumDashboardDTO> getHighestRatedAlbums() {
		Pageable limit = PageRequest.of(0, 15);
		List<String> topAlbumIds = albumRatingRepository.findTopRatedAlbumIds(limit).getContent();

		if (topAlbumIds.isEmpty()) {
			return Collections.emptyList();
		}

		return topAlbumIds.stream().map(albumId -> {
			DeezerAlbumDTO deezerDetails = deezerService.getAlbumDetails(albumId);
			Double communityScore = albumRatingRepository.findCommunityAverageRating(albumId).orElse(0.0);

			return new AlbumDashboardDTO(albumId, deezerDetails.title(), deezerDetails.coverMedium(),
					deezerDetails.artist().name(), communityScore);
		}).collect(Collectors.toList());
	}

	private Double findCurrentUserRating(String albumId) {
		User currentUser = getCurrentUserOrNull();
		if (currentUser == null) {
			return null;
		}

		Optional<AlbumRating> directAlbumRating = albumRatingRepository.findByUserAndAlbumId(currentUser, albumId);

		if (directAlbumRating.isPresent()) {
			return directAlbumRating.get().getRating();
		}

		List<TrackRating> trackRatings = trackRatingRepository.findByUserAndAlbumId(currentUser, albumId);

		if (!trackRatings.isEmpty()) {
			return trackRatings.stream().mapToDouble(TrackRating::getRating).average().orElse(0.0);
		}

		return null;
	}

	private boolean isAlbumLikedByCurrentUser(String albumId) {
		User currentUser = getCurrentUserOrNull();

		if (currentUser == null)
			return false;

		return albumLikeRepository.findByUserAndAlbumId(currentUser, albumId).isPresent();
	}

	private AlbumReviewDTO findCurrentUserReview(String albumId) {
		User currentUser = getCurrentUserOrNull();

		if (currentUser == null)
			return null;
		return albumReviewRepository.findByUserAndAlbumId(currentUser, albumId).map(this::convertReviewToDto)
				.orElse(null);
	}

	private boolean isAlbumOnListenLaterList(String albumId) {
		User currentUser = getCurrentUserOrNull();

		if (currentUser == null)
			return false;
		return listenLaterRepository.findByUserAndAlbumId(currentUser, albumId).isPresent();
	}

	private AlbumReviewDTO convertReviewToDto(AlbumReview review) {
		User currentUser = getCurrentUserOrNull();

		long likesCount = reviewLikeRepository.countByAlbumReview(review);
		boolean isLiked = (currentUser != null)
				&& reviewLikeRepository.findByUserAndAlbumReview(currentUser, review).isPresent();

		return new AlbumReviewDTO(review.getId(), review.getText(), review.getRating(), review.getCreatedAt(),
				new UserDTO(review.getUser().getId(), review.getUser().getUsername(), review.getUser().getAvatarUrl()),
				likesCount, isLiked);
	}

	private User getCurrentUserOrNull() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal()))
			return null;

		return (User) authentication.getPrincipal();
	}

	private TrackRatingDTO convertTrackRatingToDto(TrackRating rating) {
		return new TrackRatingDTO(rating.getId(), rating.getRating(), rating.getTrackId(), null);
	}
}