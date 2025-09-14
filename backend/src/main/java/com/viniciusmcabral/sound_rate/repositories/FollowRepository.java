package com.viniciusmcabral.sound_rate.repositories;

import com.viniciusmcabral.sound_rate.models.Follow;
import com.viniciusmcabral.sound_rate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

	Optional<Follow> findByFollowerAndFollowing(User follower, User following);

	void deleteByFollowerAndFollowing(User follower, User following);

	long countByFollower(User follower);

	long countByFollowing(User following);
}