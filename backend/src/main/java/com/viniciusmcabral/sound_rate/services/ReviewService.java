package com.viniciusmcabral.sound_rate.services;

import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.viniciusmcabral.sound_rate.dtos.request.ReviewRequestDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.models.AlbumReview;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumReviewRepository;
import com.viniciusmcabral.sound_rate.repositories.ReviewLikeRepository;

@Service
public class ReviewService {

	private final AlbumReviewRepository albumReviewRepository;
	private final ReviewLikeRepository reviewLikeRepository;

	public ReviewService(AlbumReviewRepository albumReviewRepository, ReviewLikeRepository reviewLikeRepository) {
		this.albumReviewRepository = albumReviewRepository;
		this.reviewLikeRepository = reviewLikeRepository;
	}

	@Transactional
	public AlbumReviewDTO createReview(ReviewRequestDTO reviewDTO) {
		User currentUser = getCurrentUser();

		albumReviewRepository.findByUserAndAlbumId(currentUser, reviewDTO.albumId()).ifPresent(review -> {
			throw new IllegalStateException("User has already reviewed this album.");
		});
		
		AlbumReview newReview = new AlbumReview(reviewDTO.albumId(), reviewDTO.text(), currentUser, reviewDTO.rating());
		AlbumReview savedReview = albumReviewRepository.save(newReview);
		
		return convertToDto(savedReview);
	}

	@Transactional
	public AlbumReviewDTO updateReview(Long reviewId, ReviewRequestDTO reviewDTO) {
		User currentUser = getCurrentUser();
		AlbumReview existingReview = albumReviewRepository.findById(reviewId)
				.orElseThrow(() -> new NoSuchElementException("Review not found with id: " + reviewId));

		if (!existingReview.getUser().getId().equals(currentUser.getId()))
			throw new AccessDeniedException("User is not the author of this review.");

		existingReview.setText(reviewDTO.text());
		existingReview.setRating(reviewDTO.rating());
		AlbumReview updatedReview = albumReviewRepository.save(existingReview);

		return convertToDto(updatedReview);
	}

	@Transactional
	public void deleteReview(Long reviewId) {
		User currentUser = getCurrentUser();
		AlbumReview reviewToDelete = albumReviewRepository.findById(reviewId)
				.orElseThrow(() -> new NoSuchElementException("Review not found with id: " + reviewId));

		if (!reviewToDelete.getUser().getId().equals(currentUser.getId()))
			throw new AccessDeniedException("User is not the author of this review.");

		albumReviewRepository.delete(reviewToDelete);
	}

	public Page<AlbumReviewDTO> getReviewsForAlbum(String albumId, Pageable pageable) {
		Page<AlbumReview> reviewPage = albumReviewRepository.findActiveReviewsByAlbumId(albumId, pageable);
		return reviewPage.map(this::convertToDto);
	}

	private User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof User)
			return (User) principal;

		throw new IllegalStateException("Could not retrieve authenticated user.");
	}

	private User getCurrentUserOrNull() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal()))
			return null;

		return (User) authentication.getPrincipal();
	}

	private AlbumReviewDTO convertToDto(AlbumReview review) {
		User currentUser = getCurrentUserOrNull();
		long likesCount = reviewLikeRepository.countByAlbumReview(review);
		boolean isLiked = (currentUser != null)
				&& reviewLikeRepository.findByUserAndAlbumReview(currentUser, review).isPresent();

		UserDTO author = new UserDTO(review.getUser().getId(), review.getUser().getUsername(),
				review.getUser().getAvatarUrl());

		return new AlbumReviewDTO(review.getId(), review.getText(), review.getRating(), review.getCreatedAt(), author,
				likesCount, isLiked);
	}
}