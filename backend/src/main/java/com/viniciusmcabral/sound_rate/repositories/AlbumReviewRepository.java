package com.viniciusmcabral.sound_rate.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusmcabral.sound_rate.models.AlbumReview;
import com.viniciusmcabral.sound_rate.models.User;

public interface AlbumReviewRepository extends JpaRepository<AlbumReview, Long> {

	List<AlbumReview> findByAlbumId(String albumId);

	Optional<AlbumReview> findByUserAndAlbumId(User user, String albumId);

	List<AlbumReview> findAllByUser(User user, Pageable pageable);

	long countByUser(User user);
}
