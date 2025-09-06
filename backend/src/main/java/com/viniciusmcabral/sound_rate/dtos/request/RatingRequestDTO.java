package com.viniciusmcabral.sound_rate.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RatingRequestDTO(

		@NotBlank(message = "Album ID cannot be blank") String albumId,

		String trackId,

		@NotNull(message = "Rating cannot be null") 
		@Min(value = 1, message = "Rating must be at least 1") 
		@Max(value = 5, message = "Rating must be at most 5") Integer rating) {
}