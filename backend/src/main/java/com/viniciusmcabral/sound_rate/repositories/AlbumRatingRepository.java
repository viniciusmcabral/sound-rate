package com.viniciusmcabral.sound_rate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusmcabral.sound_rate.models.AlbumRating;
import com.viniciusmcabral.sound_rate.models.User;

public interface AlbumRatingRepository extends JpaRepository<AlbumRating, Long>{

	Optional<AlbumRating> findByUserAndAlbumId(User user, String albumId);
}
