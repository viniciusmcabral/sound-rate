package com.viniciusmcabral.sound_rate.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDTO(
		
		@NotBlank(message = "Email cannot be blank") 
		@Email(message = "Please provide a valid email address") String email) {
}
