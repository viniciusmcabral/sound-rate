package com.viniciusmcabral.sound_rate.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusmcabral.sound_rate.models.Follow;
import com.viniciusmcabral.sound_rate.models.User;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

	Optional<Follow> findByFollowerAndFollowing(User follower, User following);

	void deleteByFollowerAndFollowing(User follower, User following);

	@Query("SELECT f FROM Follow f WHERE f.following = :user AND f.follower.active = true")
	Page<Follow> findActiveFollowersByUser(User user, Pageable pageable);

	@Query("SELECT f FROM Follow f WHERE f.follower = :user AND f.following.active = true")
	Page<Follow> findActiveFollowingByUser(User user, Pageable pageable);

	@Query("SELECT count(f) FROM Follow f WHERE f.following = :user AND f.follower.active = true")
	long countActiveFollowersByUser(@Param("user") User user);

	@Query("SELECT count(f) FROM Follow f WHERE f.follower = :user AND f.following.active = true")
	long countActiveFollowingByUser(@Param("user") User user);
}