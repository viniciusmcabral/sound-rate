package com.viniciusmcabral.sound_rate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		final String securitySchemeName = "bearerAuth";
		return new OpenAPI()
				.info(new Info().title("SoundRate API")
						.description(
								"REST API for the SoundRate application, a music rating app inspired by Letterboxd.")
						.version("v1.0"))
				.components(new Components().addSecuritySchemes(securitySchemeName,
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}
}