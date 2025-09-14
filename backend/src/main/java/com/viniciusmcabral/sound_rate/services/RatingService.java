package com.viniciusmcabral.sound_rate.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusmcabral.sound_rate.dtos.request.RatingRequestDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumRatingDTO;
import com.viniciusmcabral.sound_rate.dtos.response.TrackRatingDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.models.AlbumRating;
import com.viniciusmcabral.sound_rate.models.TrackRating;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumRatingRepository;
import com.viniciusmcabral.sound_rate.repositories.TrackRatingRepository;

@Service
public class RatingService {

	private final AlbumRatingRepository albumRatingRepository;
	private final TrackRatingRepository trackRatingRepository;

	public RatingService(AlbumRatingRepository albumRatingRepository, TrackRatingRepository trackRatingRepository) {
		this.albumRatingRepository = albumRatingRepository;
		this.trackRatingRepository = trackRatingRepository;
	}

	@Transactional
	public void rateAlbumOrTrack(RatingRequestDTO ratingDTO) {
		User currentUser = getCurrentUser();

		if (ratingDTO.trackId() != null && !ratingDTO.trackId().isBlank()) {
			rateTrack(ratingDTO, currentUser);
		} else {
			rateAlbum(ratingDTO, currentUser);
		}
	}

	@Transactional
	public void deleteRating(String albumId, String trackId) {
		User currentUser = getCurrentUser();

		if (trackId != null && !trackId.isBlank()) {
			trackRatingRepository.deleteByUserAndTrackId(currentUser, trackId);
		} else if (albumId != null && !albumId.isBlank()) {
			albumRatingRepository.deleteByUserAndAlbumId(currentUser, albumId);
		} else {
			throw new IllegalArgumentException("Either albumId or trackId must be provided to delete a rating.");
		}
	}

	public Map<String, Object> getUserRatings() {
		User currentUser = getCurrentUser();
		Pageable pageRequest = PageRequest.of(0, 20, Sort.by("id").descending());
		
		List<AlbumRatingDTO> albumRatings = albumRatingRepository.findAllByUser(currentUser, pageRequest).stream()
				.map(this::convertToAlbumRatingDTO).collect(Collectors.toList());
		List<TrackRatingDTO> trackRatings = trackRatingRepository.findAllByUser(currentUser, pageRequest).stream()
				.map(this::convertToTrackRatingDTO).collect(Collectors.toList());
	
		return Map.of("albumRatings", albumRatings, "trackRatings", trackRatings);
	}

	private User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof User) 
			return (User) principal;
		
		throw new IllegalStateException("Couldn't retrieve authenticated user.");
	}

	private AlbumRatingDTO convertToAlbumRatingDTO(AlbumRating rating) {
		UserDTO author = new UserDTO(rating.getUser().getId(), rating.getUser().getUsername(), rating.getUser().getAvatarUrl());
		return new AlbumRatingDTO(rating.getId(), rating.getRating(), author);
	}

	private TrackRatingDTO convertToTrackRatingDTO(TrackRating rating) {
		UserDTO author = new UserDTO(rating.getUser().getId(), rating.getUser().getUsername(), rating.getUser().getAvatarUrl());
		return new TrackRatingDTO(rating.getId(), rating.getRating(), rating.getTrackId(), author);
	}

	private void rateAlbum(RatingRequestDTO ratingDTO, User user) {
		albumRatingRepository.findByUserAndAlbumId(user, ratingDTO.albumId()).ifPresentOrElse(existingRating -> {
			existingRating.setRating(ratingDTO.rating());
			albumRatingRepository.save(existingRating);
		}, () -> {
			AlbumRating newRating = new AlbumRating(ratingDTO.albumId(), ratingDTO.rating(), user);
			albumRatingRepository.save(newRating);
		});
	}

	private void rateTrack(RatingRequestDTO ratingDTO, User user) {
		trackRatingRepository.findByUserAndTrackId(user, ratingDTO.trackId()).ifPresentOrElse(existingRating -> {
			existingRating.setRating(ratingDTO.rating());
			trackRatingRepository.save(existingRating);
		}, () -> {
			TrackRating newRating = new TrackRating(ratingDTO.albumId(), ratingDTO.trackId(), ratingDTO.rating(), user);
			trackRatingRepository.save(newRating);
		});
	}
}