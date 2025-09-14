package com.viniciusmcabral.sound_rate.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.viniciusmcabral.sound_rate.models.ListenLater;
import com.viniciusmcabral.sound_rate.models.User;

@Repository
public interface ListenLaterRepository extends JpaRepository<ListenLater, Long> {

	Optional<ListenLater> findByUserAndAlbumId(User user, String albumId);

	void deleteByUserAndAlbumId(User user, String albumId);

	Page<ListenLater> findByUser(User user, Pageable pageable);
}