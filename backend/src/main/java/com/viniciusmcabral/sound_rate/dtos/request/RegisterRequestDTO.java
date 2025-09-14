package com.viniciusmcabral.sound_rate.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

		@NotBlank(message = "Username can't be blank") 
		@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") String username,

		@NotBlank(message = "Email can't be blank") 
		@Email(message = "Email should be in a valid format") String email,

		@NotBlank(message = "Password can't be blank") 
		@Size(min = 6, message = "Password must have at least 6 characters") String password) {
}