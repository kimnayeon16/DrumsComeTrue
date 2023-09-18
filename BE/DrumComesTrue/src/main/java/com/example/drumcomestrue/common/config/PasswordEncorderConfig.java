package com.example.drumcomestrue.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncorderConfig{

	@Bean
	public PasswordEncoder passwordEncoder() {
		//return new MessageDigestPasswordEncoder("SHA-256");
		return new BCryptPasswordEncoder();
	}
}
