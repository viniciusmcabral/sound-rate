package com.viniciusmcabral.sound_rate.dtos.response;

public record TrackRatingDTO(Long id, Integer rating, String trackId, UserDTO author) {

}
