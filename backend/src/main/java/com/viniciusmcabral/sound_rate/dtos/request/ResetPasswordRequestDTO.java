package com.viniciusmcabral.sound_rate.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDTO(

		@NotBlank(message = "Token cannot be blank") String token,

		@NotBlank(message = "New password cannot be blank") 
		@Size(min = 6, message = "Password must be at least 6 characters long") String newPassword) {
}
