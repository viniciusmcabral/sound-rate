package com.viniciusmcabral.sound_rate.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.viniciusmcabral.sound_rate.dtos.response.AlbumRatingDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.dtos.response.TrackRatingDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserProfileDTO;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumRatingRepository;
import com.viniciusmcabral.sound_rate.repositories.AlbumReviewRepository;
import com.viniciusmcabral.sound_rate.repositories.TrackRatingRepository;
import com.viniciusmcabral.sound_rate.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final AlbumReviewRepository albumReviewRepository;
	private final AlbumRatingRepository albumRatingRepository;
	private final TrackRatingRepository trackRatingRepository;

	public UserService(UserRepository userRepository, AlbumReviewRepository albumReviewRepository,
			AlbumRatingRepository albumRatingRepository, TrackRatingRepository trackRatingRepository) {
		this.userRepository = userRepository;
		this.albumReviewRepository = albumReviewRepository;
		this.albumRatingRepository = albumRatingRepository;
		this.trackRatingRepository = trackRatingRepository;
	}

	public UserProfileDTO getUserProfile(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new NoSuchElementException("User not found with username: " + username));

		UserDTO userDto = new UserDTO(user.getId(), user.getUsername());

		PageRequest pageRequestReviews = PageRequest.of(0, 5, Sort.by("createdAt").descending());
		List<AlbumReviewDTO> recentReviews = albumReviewRepository.findAllByUser(user, pageRequestReviews).stream()
				.map(review -> new AlbumReviewDTO(review.getId(), review.getText(), review.getCreatedAt(), userDto))
				.collect(Collectors.toList());

		PageRequest pageRequestRatings = PageRequest.of(0, 5, Sort.by("id").descending());
		List<AlbumRatingDTO> recentAlbumRatings = albumRatingRepository.findAllByUser(user, pageRequestRatings).stream()
				.map(rating -> new AlbumRatingDTO(rating.getId(), rating.getRating(), userDto))
				.collect(Collectors.toList());

		List<TrackRatingDTO> recentTrackRatings = trackRatingRepository.findAllByUser(user, pageRequestRatings).stream()
				.map(rating -> new TrackRatingDTO(rating.getId(), rating.getRating(), rating.getTrackId(), userDto))
				.collect(Collectors.toList());

		long totalReviews = albumReviewRepository.countByUser(user);
		long totalAlbumRatings = albumRatingRepository.countByUser(user);
		long totalTrackRatings = trackRatingRepository.countByUser(user);

		return new UserProfileDTO(userDto, totalReviews, totalAlbumRatings, totalTrackRatings, recentReviews, recentAlbumRatings, recentTrackRatings);
	}
}