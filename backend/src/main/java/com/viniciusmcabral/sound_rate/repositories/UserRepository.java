package com.viniciusmcabral.sound_rate.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusmcabral.sound_rate.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsernameAndActiveTrue(String username);

	Optional<User> findByEmailAndActiveTrue(String email);

	@Query("SELECT u FROM User u WHERE (u.username = :login OR u.email = :login) AND u.active = true")
	Optional<User> findByLoginAndActiveTrue(@Param("login") String login);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.active = true AND lower(u.username) LIKE lower(concat('%', :query, '%'))")
    Page<User> searchByUsername(String query, Pageable pageable);
}
