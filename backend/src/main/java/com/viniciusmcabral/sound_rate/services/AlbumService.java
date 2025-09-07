package com.viniciusmcabral.sound_rate.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.viniciusmcabral.sound_rate.dtos.response.AlbumDetailsDTO;
import com.viniciusmcabral.sound_rate.dtos.response.AlbumReviewDTO;
import com.viniciusmcabral.sound_rate.dtos.response.CriticReviewDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.dtos.spotify.SpotifyAlbumDTO;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumRatingRepository;
import com.viniciusmcabral.sound_rate.repositories.AlbumReviewRepository;
import com.viniciusmcabral.sound_rate.repositories.CriticReviewRepository;

@Service
public class AlbumService {
	
	private final SpotifyService spotifyService;
	private final AlbumRatingRepository albumRatingRepository;
	private final AlbumReviewRepository albumReviewRepository;
	private final CriticReviewRepository criticReviewRepository;
	private final ScraperService scraperService;

	public AlbumService(SpotifyService spotifyService, AlbumRatingRepository albumRatingRepository,
			AlbumReviewRepository albumReviewRepository, CriticReviewRepository criticReviewRepository, ScraperService scraperService) {
		this.spotifyService = spotifyService;
		this.albumRatingRepository = albumRatingRepository;
		this.albumReviewRepository = albumReviewRepository;
		this.criticReviewRepository = criticReviewRepository;
		this.scraperService = scraperService;
	}

	public AlbumDetailsDTO getAlbumDetails(String albumId) {
		SpotifyAlbumDTO spotifyDetails = Optional.ofNullable(spotifyService.getAlbumDetails(albumId))
				.orElseThrow(() -> new NoSuchElementException("Album not found on Spotify with ID: " + albumId));

		CriticReviewDTO criticReview = criticReviewRepository.findByAlbumId(albumId)
		        .map(cr -> new CriticReviewDTO(cr.getSource(), cr.getScore(), cr.getReviewUrl()))
		        .orElseGet(() -> {
		            String artistName = spotifyDetails.artists().get(0).name();
		            String albumName = spotifyDetails.name();
		            scraperService.fetchAndSavePitchforkScore(artistName, albumName, albumId);	            
		            return null;
		        });

		Double communityScore = albumRatingRepository.findCommunityAverageRating(albumId).orElse(null);

		List<AlbumReviewDTO> userReviews = albumReviewRepository.findByAlbumId(albumId).stream()
				.map(review -> new AlbumReviewDTO(review.getId(), review.getText(), review.getCreatedAt(),
						new UserDTO(review.getUser().getId(), review.getUser().getUsername())))
				.collect(Collectors.toList());

		Integer currentUserRating = findCurrentUserRating(albumId);

		return new AlbumDetailsDTO(spotifyDetails, criticReview, communityScore, currentUserRating, userReviews);
	}

	private Integer findCurrentUserRating(String albumId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
			return null;
		}

		User currentUser = (User) authentication.getPrincipal();

		return albumRatingRepository.findByUserAndAlbumId(currentUser, albumId).map(rating -> rating.getRating()) .orElse(null); 
	}
}
