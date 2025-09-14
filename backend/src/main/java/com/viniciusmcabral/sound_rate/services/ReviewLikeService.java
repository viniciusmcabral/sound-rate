package com.viniciusmcabral.sound_rate.services;

import java.util.NoSuchElementException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusmcabral.sound_rate.models.AlbumReview;
import com.viniciusmcabral.sound_rate.models.ReviewLike;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumReviewRepository;
import com.viniciusmcabral.sound_rate.repositories.ReviewLikeRepository;

@Service
public class ReviewLikeService {

	private final ReviewLikeRepository reviewLikeRepository;
	private final AlbumReviewRepository albumReviewRepository;

	public ReviewLikeService(ReviewLikeRepository reviewLikeRepository, AlbumReviewRepository albumReviewRepository) {
		this.reviewLikeRepository = reviewLikeRepository;
		this.albumReviewRepository = albumReviewRepository;
	}

	@Transactional
	public void likeReview(Long reviewId) {
		User currentUser = getCurrentUser();
		AlbumReview review = albumReviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("Review not found with id: " + reviewId));

		if (reviewLikeRepository.findByUserAndAlbumReview(currentUser, review).isEmpty()) {
			ReviewLike newLike = new ReviewLike(currentUser, review);
			reviewLikeRepository.save(newLike);
		}
	}

	@Transactional
	public void unlikeReview(Long reviewId) {
		User currentUser = getCurrentUser();
		AlbumReview review = albumReviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("Review not found with id: " + reviewId));
		reviewLikeRepository.deleteByUserAndAlbumReview(currentUser, review);
	}

	private User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}