package com.viniciusmcabral.sound_rate.repositories;

import com.viniciusmcabral.sound_rate.models.AlbumReview;
import com.viniciusmcabral.sound_rate.models.ReviewLike;
import com.viniciusmcabral.sound_rate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

	Optional<ReviewLike> findByUserAndAlbumReview(User user, AlbumReview albumReview);

	void deleteByUserAndAlbumReview(User user, AlbumReview albumReview);

	long countByAlbumReview(AlbumReview albumReview);
}