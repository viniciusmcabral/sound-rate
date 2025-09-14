package com.viniciusmcabral.sound_rate.services;

import com.viniciusmcabral.sound_rate.models.AlbumLike;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.AlbumLikeRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlbumLikeService {

	private final AlbumLikeRepository albumLikeRepository;

	public AlbumLikeService(AlbumLikeRepository albumLikeRepository) {
		this.albumLikeRepository = albumLikeRepository;
	}

	@Transactional
	public void likeAlbum(String albumId) {
		User currentUser = getCurrentUser();

		if (albumLikeRepository.findByUserAndAlbumId(currentUser, albumId).isEmpty()) {
			AlbumLike newLike = new AlbumLike(currentUser, albumId);
			albumLikeRepository.save(newLike);
		}
	}

	@Transactional
	public void unlikeAlbum(String albumId) {
		User currentUser = getCurrentUser();
		albumLikeRepository.deleteByUserAndAlbumId(currentUser, albumId);
	}

	private User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}