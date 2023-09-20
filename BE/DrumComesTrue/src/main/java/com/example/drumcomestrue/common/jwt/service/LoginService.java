package com.example.drumcomestrue.common.jwt.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.drumcomestrue.common.exception.ApplicationError;
import com.example.drumcomestrue.db.entity.User;
import com.example.drumcomestrue.db.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class LoginService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		log.info(userId);
		User user = userRepository.findByUserId(userId)
			.orElseThrow(() -> new UsernameNotFoundException(ApplicationError.MEMBER_NOT_FOUND.getMessage()));
		log.info(user.getUserId());
		return org.springframework.security.core.userdetails.User.builder()
			.username(user.getUserId())
			.password(user.getUserPw())
			.roles(user.getRole().name())
			.build();
	}
}