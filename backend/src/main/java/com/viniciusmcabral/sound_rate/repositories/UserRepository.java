package com.viniciusmcabral.sound_rate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusmcabral.sound_rate.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsernameOrEmail(String username, String email);
}
