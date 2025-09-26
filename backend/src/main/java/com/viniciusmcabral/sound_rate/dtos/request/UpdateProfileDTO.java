package com.viniciusmcabral.sound_rate.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileDTO(

		@NotBlank(message = "Email cannot be blank") 
		@Email(message = "Email should be valid") String email) {
}