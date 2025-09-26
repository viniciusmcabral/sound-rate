package com.viniciusmcabral.sound_rate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viniciusmcabral.sound_rate.models.PasswordResetToken;
import com.viniciusmcabral.sound_rate.models.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	Optional<PasswordResetToken> findByToken(String token);

	Optional<PasswordResetToken> findByUser(User user);

	@Modifying
	@Query("DELETE FROM PasswordResetToken t WHERE t.user = :user")
	void deleteByUser(@Param("user") User user);
}