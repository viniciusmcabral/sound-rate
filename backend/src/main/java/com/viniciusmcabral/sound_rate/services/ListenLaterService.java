package com.viniciusmcabral.sound_rate.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.viniciusmcabral.sound_rate.dtos.deezer.DeezerAlbumDTO;
import com.viniciusmcabral.sound_rate.models.ListenLater;
import com.viniciusmcabral.sound_rate.models.User;
import com.viniciusmcabral.sound_rate.repositories.ListenLaterRepository;

@Service
public class ListenLaterService {

	private final ListenLaterRepository listenLaterRepository;
	private final DeezerService deezerService;

	public ListenLaterService(ListenLaterRepository listenLaterRepository, DeezerService deezerService) {
		this.listenLaterRepository = listenLaterRepository;
		this.deezerService = deezerService;
	}

	@Transactional
	public void addAlbum(String albumId) {
		User currentUser = getCurrentUser();
		
		if (listenLaterRepository.findByUserAndAlbumId(currentUser, albumId).isEmpty()) {
			ListenLater newEntry = new ListenLater(currentUser, albumId);
			listenLaterRepository.save(newEntry);
		}
	}

	@Transactional
	public void removeAlbum(String albumId) {
		User currentUser = getCurrentUser();
		listenLaterRepository.deleteByUserAndAlbumId(currentUser, albumId);
	}

	@Transactional(readOnly = true)
	public Page<DeezerAlbumDTO> getListenLaterList(Pageable pageable) {
		User currentUser = getCurrentUser();
		Page<ListenLater> entries = listenLaterRepository.findByUser(currentUser, pageable);
		
		return entries.map(entry -> deezerService.getAlbumDetails(entry.getAlbumId()));
	}

	private User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}