package com.example.drumcomestrue.api.service;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.drumcomestrue.api.request.user.LoginRequest;
import com.example.drumcomestrue.api.request.user.SignupRequest;
import com.example.drumcomestrue.api.response.user.FindIdResponse;
import com.example.drumcomestrue.api.response.user.FindPwResponse;
import com.example.drumcomestrue.api.response.user.FindResponse;
// import com.example.drumcomestrue.common.config.PasswordEncorderConfig;
import com.example.drumcomestrue.common.error.DuplicateException;
import com.example.drumcomestrue.common.exception.ApplicationError;
import com.example.drumcomestrue.db.entity.User;
import com.example.drumcomestrue.db.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	// private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;
	public void signup(SignupRequest signupRequest) {

		check(signupRequest.getUserId());

		User user = User.builder()
			.userId(signupRequest.getUserId())
			// .userPw(passwordEncoder.encode(signupRequest.getUserPw()))
			.userPw(signupRequest.getUserPw())
			.userName(signupRequest.getUserName())
			.phoneNumber(signupRequest.getUserNumber())
			.build();
		userRepository.save(user);
	}

	public void login(LoginRequest loginRequest) {

	}

	public FindIdResponse findId(String userId) {
		return null;
	}

	public FindPwResponse findPw(String userPhone) {
		return null;
	}

	public FindResponse verify(String phoneNumber) {
		return null;
	}

	public void check(String userId) {
		if(userRepository.existsByUserId(userId)){
			throw new DuplicateException(ApplicationError.DUPLICATE_USERID);
		}
	}
}
