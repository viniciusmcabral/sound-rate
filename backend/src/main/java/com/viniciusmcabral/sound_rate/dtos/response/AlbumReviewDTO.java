package com.viniciusmcabral.sound_rate.dtos.response;

import java.time.LocalDateTime;

public record AlbumReviewDTO(Long id, String text, LocalDateTime createdAt, UserDTO author) {

}
