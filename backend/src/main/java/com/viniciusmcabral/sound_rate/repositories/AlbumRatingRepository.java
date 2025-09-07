package com.viniciusmcabral.sound_rate.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viniciusmcabral.sound_rate.models.AlbumRating;
import com.viniciusmcabral.sound_rate.models.User;

public interface AlbumRatingRepository extends JpaRepository<AlbumRating, Long> {

	Optional<AlbumRating> findByUserAndAlbumId(User user, String albumId);

	void deleteByUserAndAlbumId(User user, String albumId);

	List<AlbumRating> findAllByUser(User user, Pageable pageable);

	@Query("SELECT AVG(ar.rating) FROM AlbumRating ar WHERE ar.albumId = :albumId")
	Optional<Double> findCommunityAverageRating(String albumId);

	long countByUser(User user);
}
