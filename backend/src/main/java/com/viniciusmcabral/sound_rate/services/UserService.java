package com.viniciusmcabral.sound_rate.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import com.viniciusmcabral.sound_rate.dtos.request.UpdatePasswordDTO;
import com.viniciusmcabral.sound_rate.dtos.request.UpdateProfileDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserProfileDTO;
import com.viniciusmcabral.sound_rate.dtos.response.UserRatingDTO;
import com.viniciusmcabral.sound_rate.models.AlbumLike;
import com.viniciusmcabral.sound_rate.models.AlbumRating;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumLikeRepository;
import com.viniciusmcabral.sound_rate.repositories.AlbumRatingRepository;
import com.viniciusmcabral.sound_rate.repositories.AlbumReviewRepository;
import com.viniciusmcabral.sound_rate.repositories.FollowRepository;
import com.viniciusmcabral.sound_rate.repositories.TrackRatingRepository;
import com.viniciusmcabral.sound_rate.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AlbumReviewRepository albumReviewRepository;
	private final AlbumRatingRepository albumRatingRepository;
	private final TrackRatingRepository trackRatingRepository;
	private final DeezerService deezerService;
	private final StorageService storageService;
	private final AlbumLikeRepository albumLikeRepository;
	private final FollowRepository followRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AlbumReviewRepository albumReviewRepository, AlbumRatingRepository albumRatingRepository,
			TrackRatingRepository trackRatingRepository, DeezerService deezerService, StorageService storageService,
			AlbumLikeRepository albumLikeRepository, FollowRepository followRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.albumReviewRepository = albumReviewRepository;
		this.albumRatingRepository = albumRatingRepository;
		this.trackRatingRepository = trackRatingRepository;
		this.deezerService = deezerService;
		this.storageService = storageService;
		this.albumLikeRepository = albumLikeRepository;
		this.followRepository = followRepository;
	}

	@Transactional(readOnly = true)
	public UserProfileDTO getUserProfile(String username) {
		User user = userRepository.findByUsernameAndActiveTrue(username).orElseThrow(() -> new NoSuchElementException("User not found with username: " + username));
		User currentUser = getCurrentUserOrNull();
		
		long followersCount = followRepository.countByFollowing(user);
		long followingCount = followRepository.countByFollower(user);
		boolean isFollowed = (currentUser != null) && followRepository.findByFollowerAndFollowing(currentUser, user).isPresent();
		long totalReviews = albumReviewRepository.countByUser(user);
		long totalAlbumRatings = albumRatingRepository.countByUser(user);
		long totalTrackRatings = trackRatingRepository.countByUser(user);
		
		return new UserProfileDTO(new UserDTO(user.getId(), user.getUsername(), user.getAvatarUrl()), totalReviews, 
				totalAlbumRatings, totalTrackRatings, followersCount, followingCount, isFollowed);
	}

	@Transactional(readOnly = true)
	public Page<UserRatingDTO> getRatedAlbumsPage(String username, Pageable pageable) {
		User user = userRepository.findByUsernameAndActiveTrue(username).orElseThrow(() -> new NoSuchElementException("User not found: " + username));
		Page<AlbumRating> ratingsPage = albumRatingRepository.findByUser(user, pageable);
		List<UserRatingDTO> dtoList = ratingsPage.getContent().stream().map(rating -> {
			DeezerAlbumDTO albumDetails = deezerService.getAlbumDetails(String.valueOf(rating.getAlbumId()));
			
			if (albumDetails == null)
				return null;

			String reviewText = albumReviewRepository.findByUserAndAlbumId(user, rating.getAlbumId()).map(review -> review.getText()).orElse(null);
			
			return new UserRatingDTO(albumDetails, rating.getRating(), rating.getCreatedAt(), reviewText);
		}).filter(Objects::nonNull).collect(Collectors.toList());
		
		return new PageImpl<>(dtoList, pageable, ratingsPage.getTotalElements());
	}

	@Transactional(readOnly = true)
	public Page<DeezerAlbumDTO> getLikedAlbumsPage(String username, Pageable pageable) {
		User user = userRepository.findByUsernameAndActiveTrue(username).orElseThrow(() -> new NoSuchElementException("User not found: " + username));
		Page<AlbumLike> likedAlbumsPage = albumLikeRepository.findByUser(user, pageable);
		
		return likedAlbumsPage.map(like -> deezerService.getAlbumDetails(like.getAlbumId()));
	}

	@Transactional
	public void deleteCurrentUser() {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userToDelete = userRepository.findById(currentUser.getId()).orElseThrow(() -> new NoSuchElementException("User not found for deletion."));
		
		userToDelete.setActive(false);
		
		userRepository.save(userToDelete);
	}

	@Transactional
	public UserDTO updateProfile(User currentUser, UpdateProfileDTO data) {
		userRepository.findByEmail(data.email()).ifPresent(user -> {
			if (!user.getId().equals(currentUser.getId())) 
				throw new IllegalStateException("Email already in use by another account.");
		});

		currentUser.setEmail(data.email());
		User updatedUser = userRepository.save(currentUser);
		
		return new UserDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getAvatarUrl());
	}

	@Transactional
	public void updatePassword(User currentUser, UpdatePasswordDTO data) {
		if (!passwordEncoder.matches(data.currentPassword(), currentUser.getPassword())) 
			throw new AccessDeniedException("Incorrect current password.");
		
		String newHashedPassword = passwordEncoder.encode(data.newPassword());
		currentUser.setPassword(newHashedPassword);
		
		userRepository.save(currentUser);
	}

	@Transactional
	public UserDTO updateAvatar(User currentUser, MultipartFile file) {
		String newAvatarUrl = storageService.uploadFile(file);
		currentUser.setAvatarUrl(newAvatarUrl);
		
		userRepository.save(currentUser);
		
		return new UserDTO(currentUser.getId(), currentUser.getUsername(), currentUser.getAvatarUrl());
	}

	@Transactional
	public UserDTO resetAvatar(User currentUser) {
		String defaultAvatarUrl = "https://api.dicebear.com/8.x/initials/svg?seed=" + currentUser.getUsername();
		currentUser.setAvatarUrl(defaultAvatarUrl);
		
		User updatedUser = userRepository.save(currentUser);
		
		return new UserDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getAvatarUrl());
	}

	private User getCurrentUserOrNull() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal()))
			return null;
		
		return (User) authentication.getPrincipal();
	}
}