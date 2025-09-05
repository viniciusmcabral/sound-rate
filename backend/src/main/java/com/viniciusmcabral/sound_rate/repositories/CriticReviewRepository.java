package com.viniciusmcabral.sound_rate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusmcabral.sound_rate.models.CriticReview;

public interface CriticReviewRepository extends JpaRepository<CriticReview, Long>{
	
	Optional<CriticReview> findByAlbumId(String albumId);
}
