package com.viniciusmcabral.sound_rate.dtos.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RatingRequestDTO(

		@NotBlank(message = "Album ID cannot be blank") String albumId,
		String trackId,

		@NotNull(message = "Rating cannot be null") 
		@DecimalMin(value = "0.5", message = "Rating must be at least 0.5") 
		@DecimalMax(value = "5.0", message = "Rating must be at most 5.0") Double rating) {
}