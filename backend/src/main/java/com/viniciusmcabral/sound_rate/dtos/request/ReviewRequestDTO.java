package com.viniciusmcabral.sound_rate.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequestDTO(

		@NotBlank(message = "Album ID can't be blank") String albumId,

		@NotBlank(message = "Review text can't be blank") 
		@Size(min = 10, max = 10000, message = "Review must be between 10 and 10000 characters") String text,

		@NotNull(message = "Rating can't be null") 
		@Min(value = 1, message = "Rating must be at least 1") 
		@Max(value = 5, message = "Rating must be at most 5") Double rating){
}