package com.viniciusmcabral.sound_rate.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viniciusmcabral.sound_rate.models.TrackRating;
import com.viniciusmcabral.sound_rate.models.User;

public interface TrackRatingRepository extends JpaRepository<TrackRating, Long> {

	List<TrackRating> findByUserAndAlbumId(User user, String albumId);

	Optional<TrackRating> findByUserAndTrackId(User user, String trackId);

	Optional<TrackRating> findByUserAndAlbumIdAndTrackId(User user, String albumId, String trackId);

	@Query("SELECT AVG(tr.rating) FROM TrackRating tr WHERE tr.albumId = :albumId")
	Optional<Double> findAverageRatingByAlbumId(String albumId);

	void deleteByUserAndTrackId(User user, String trackId);

	void deleteAllByUserAndAlbumId(User user, String albumId);

	List<TrackRating> findAllByUser(User user, Pageable pagealble);

	long countByUser(User user);
}
