package com.viniciusmcabral.sound_rate.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewRequestDTO(

		@NotBlank(message = "Album ID can't be blank") String albumId,

		@NotBlank(message = "Review text can't be blank") 
		@Size(min = 10, max = 10000, message = "Review must be between 10 and 10000 characters") String text) {
}