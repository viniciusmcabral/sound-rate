package com.viniciusmcabral.sound_rate.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.viniciusmcabral.sound_rate.config.security.filters.SecurityFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	private final SecurityFilter securityFilter;
	private final CorsConfigurationSource corsConfigurationSource;

	public SecurityConfig(SecurityFilter securityFilter, CorsConfigurationSource corsConfigurationSource) {
		this.securityFilter = securityFilter;
		this.corsConfigurationSource = corsConfigurationSource;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource))
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(req -> {
					req.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
					req.requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll();
					req.requestMatchers(HttpMethod.GET, "/api/v1/users/**").permitAll();
					req.requestMatchers(HttpMethod.GET, "/api/v1/albums/**").permitAll();
					req.requestMatchers(HttpMethod.GET, "/api/v1/artists/**").permitAll();
					req.requestMatchers(HttpMethod.GET, "/api/v1/search").permitAll();
					req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
					req.anyRequest().authenticated();
				}).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
