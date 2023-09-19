// package com.example.drumcomestrue;
//
// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
//
// @EnableWebSecurity
// @TestConfiguration
// public class TestSecurityConfig {
//
// 	@Bean
// 	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
// 		http
// 			.authorizeRequests()
// 			.antMatchers("/**").permitAll() // 모든 요청을 허용
// 			.and()
// 			.sessionManagement()
// 			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
// 			.and()
// 			.csrf()
// 			.disable()
// 			.headers()
// 			.frameOptions()
// 			.disable();
// 		return http.build();
// 	}
// }