package com.viniciusmcabral.sound_rate.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viniciusmcabral.sound_rate.models.AlbumReview;
import com.viniciusmcabral.sound_rate.models.User;

public interface AlbumReviewRepository extends JpaRepository<AlbumReview, Long> {

	Optional<AlbumReview> findByUserAndAlbumId(User user, String albumId);

	List<AlbumReview> findAllByUser(User user, Pageable pageable);

	long countByUser(User user);

	@Query("SELECT r.albumId FROM AlbumReview r WHERE r.user = :user ORDER BY r.createdAt DESC")
	List<String> findAllAlbumIdsByUser(User user);

	@Query("SELECT r FROM AlbumReview r WHERE r.albumId = :albumId AND r.user.active = true")
	Page<AlbumReview> findActiveReviewsByAlbumId(@Param("albumId") String albumId, Pageable pageable);
}
