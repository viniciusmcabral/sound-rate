package com.viniciusmcabral.sound_rate.services;

import com.viniciusmcabral.sound_rate.dtos.request.ReviewRequestDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.models.AlbumReview;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumReviewRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ReviewService {

	private final AlbumReviewRepository albumReviewRepository;

	public ReviewService(AlbumReviewRepository albumReviewRepository) {
		this.albumReviewRepository = albumReviewRepository;
	}

	@Transactional
	public AlbumReviewDTO createReview(ReviewRequestDTO reviewDTO) {
		User currentUser = getCurrentUser();

		albumReviewRepository.findByUserAndAlbumId(currentUser, reviewDTO.albumId()).ifPresent(review -> {
			throw new IllegalStateException("User has already reviewed this album.");
		});

		AlbumReview newReview = new AlbumReview(reviewDTO.albumId(), reviewDTO.text(), currentUser);

		AlbumReview savedReview = albumReviewRepository.save(newReview);
		return convertToDto(savedReview);
	}

	@Transactional
	public AlbumReviewDTO updateReview(Long reviewId, ReviewRequestDTO reviewDTO) {
		User currentUser = getCurrentUser();

		AlbumReview existingReview = albumReviewRepository.findById(reviewId)
				.orElseThrow(() -> new NoSuchElementException("Review not found with id: " + reviewId));

		if (!existingReview.getUser().getId().equals(currentUser.getId())) {
			throw new AccessDeniedException("User is not the author of this review.");
		}

		existingReview.setText(reviewDTO.text());
		AlbumReview updatedReview = albumReviewRepository.save(existingReview);
		return convertToDto(updatedReview);
	}

	@Transactional
	public void deleteReview(Long reviewId) {
		User currentUser = getCurrentUser();

		AlbumReview reviewToDelete = albumReviewRepository.findById(reviewId)
				.orElseThrow(() -> new NoSuchElementException("Review not found with id: " + reviewId));

		if (!reviewToDelete.getUser().getId().equals(currentUser.getId())) {
			throw new AccessDeniedException("User is not the author of this review.");
		}

		albumReviewRepository.delete(reviewToDelete);
	}

	public List<AlbumReviewDTO> getReviewsForAlbum(String albumId) {
		return albumReviewRepository.findByAlbumId(albumId).stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	private User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			return (User) principal;
		}
		throw new IllegalStateException("Could not retrieve authenticated user.");
	}

	private AlbumReviewDTO convertToDto(AlbumReview review) {
		UserDTO author = new UserDTO(review.getUser().getId(), review.getUser().getUsername());
		return new AlbumReviewDTO(review.getId(), review.getText(), review.getCreatedAt(), author);
	}
}