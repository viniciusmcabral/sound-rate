package com.viniciusmcabral.sound_rate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SoundRateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoundRateApplication.class, args);
	}

}
