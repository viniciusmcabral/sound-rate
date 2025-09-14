package com.viniciusmcabral.sound_rate.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viniciusmcabral.sound_rate.models.AlbumLike;
import com.viniciusmcabral.sound_rate.models.User;

@Repository
public interface AlbumLikeRepository extends JpaRepository<AlbumLike, Long> {

	Optional<AlbumLike> findByUserAndAlbumId(User user, String albumId);

	void deleteByUserAndAlbumId(User user, String albumId);

	long countByAlbumId(String albumId);

	Page<AlbumLike> findByUser(User user, Pageable pageable);
}
