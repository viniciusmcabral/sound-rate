package com.viniciusmcabral.sound_rate.services;

import com.viniciusmcabral.sound_rate.models.Follow;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.FollowRepository;
import com.viniciusmcabral.sound_rate.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Service
public class FollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	public FollowService(FollowRepository followRepository, UserRepository userRepository) {
		this.followRepository = followRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public void followUser(String usernameToFollow, User currentUser) {
		User userToFollow = userRepository.findByUsernameAndActiveTrue(usernameToFollow)
				.orElseThrow(() -> new NoSuchElementException("User to follow not found: " + usernameToFollow));

		if (currentUser.getId().equals(userToFollow.getId()))
			throw new IllegalArgumentException("You cannot follow yourself.");

		if (followRepository.findByFollowerAndFollowing(currentUser, userToFollow).isEmpty()) {
			Follow newFollow = new Follow(currentUser, userToFollow);
			followRepository.save(newFollow);
		}
	}

	@Transactional
	public void unfollowUser(String usernameToUnfollow, User currentUser) {
		User userToUnfollow = userRepository.findByUsernameAndActiveTrue(usernameToUnfollow)
				.orElseThrow(() -> new NoSuchElementException("User to unfollow not found: " + usernameToUnfollow));
		followRepository.deleteByFollowerAndFollowing(currentUser, userToUnfollow);
	}
}