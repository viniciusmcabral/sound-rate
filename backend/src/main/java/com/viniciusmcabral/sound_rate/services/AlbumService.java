package com.viniciusmcabral.sound_rate.services;

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
import com.viniciusmcabral.sound_rate.dtos.response.AlbumDetailsDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.models.AlbumReview;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumLikeRepository;
import com.viniciusmcabral.sound_rate.repositories.AlbumRatingRepository;
import com.viniciusmcabral.sound_rate.repositories.AlbumReviewRepository;
import com.viniciusmcabral.sound_rate.repositories.ListenLaterRepository;
import com.viniciusmcabral.sound_rate.repositories.ReviewLikeRepository;

@Service
public class AlbumService {

	private final DeezerService deezerService;
	private final AlbumRatingRepository albumRatingRepository;
	private final AlbumReviewRepository albumReviewRepository;
	private final AlbumLikeRepository albumLikeRepository;
	private final ReviewLikeRepository reviewLikeRepository;
	private final ListenLaterRepository listenLaterRepository;

	public AlbumService(DeezerService deezerService, AlbumRatingRepository albumRatingRepository,
			AlbumReviewRepository albumReviewRepository, AlbumLikeRepository albumLikeRepository,
			ReviewLikeRepository reviewLikeRepository, ListenLaterRepository listenLaterRepository) {
		this.deezerService = deezerService;
		this.albumRatingRepository = albumRatingRepository;
		this.albumReviewRepository = albumReviewRepository;
		this.albumLikeRepository = albumLikeRepository;
		this.reviewLikeRepository = reviewLikeRepository;
		this.listenLaterRepository = listenLaterRepository;
	}

	@Transactional(readOnly = true)
	public AlbumDetailsDTO getAlbumDetails(String albumId) {
		DeezerAlbumDTO deezerDetails = Optional.ofNullable(deezerService.getAlbumDetails(albumId))
				.orElseThrow(() -> new NoSuchElementException("Album not found on Deezer with ID: " + albumId));

		Double communityScore = albumRatingRepository.findCommunityAverageRating(albumId).orElse(null);

		Pageable firstPageOfReviews = PageRequest.of(0, 10, Sort.by("createdAt").descending());
		List<AlbumReviewDTO> userReviews = albumReviewRepository.findByAlbumId(albumId, firstPageOfReviews).getContent()
				.stream().map(this::convertReviewToDto) 
				.collect(Collectors.toList());

		Double currentUserRating = findCurrentUserRating(albumId);
		AlbumReviewDTO currentUserReview = findCurrentUserReview(albumId);

		long likesCount = albumLikeRepository.countByAlbumId(albumId);
		boolean isLikedByCurrentUser = isAlbumLikedByCurrentUser(albumId);
		boolean isOnListenLaterList = isAlbumOnListenLaterList(albumId);

		return new AlbumDetailsDTO(deezerDetails, communityScore, currentUserRating, currentUserReview, userReviews, likesCount, isLikedByCurrentUser, isOnListenLaterList);
	}

	private Double findCurrentUserRating(String albumId) {
		User currentUser = getCurrentUserOrNull();
		
		if (currentUser == null)
			return null;

		return albumRatingRepository.findByUserAndAlbumId(currentUser, albumId).map(rating -> rating.getRating()).orElse(null);
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
		return albumReviewRepository.findByUserAndAlbumId(currentUser, albumId).map(this::convertReviewToDto).orElse(null);
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
		boolean isLiked = (currentUser != null) && reviewLikeRepository.findByUserAndAlbumReview(currentUser, review).isPresent();
		
		return new AlbumReviewDTO(review.getId(), review.getText(), review.getCreatedAt(),
				new UserDTO(review.getUser().getId(), review.getUser().getUsername(), review.getUser().getAvatarUrl()), likesCount, isLiked);
	}

	private User getCurrentUserOrNull() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) 
			return null;
		
		return (User) authentication.getPrincipal();
	}
}