package com.viniciusmcabral.sound_rate.dtos.deezer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeezerContributorDTO(long id, String name, String role) {
}