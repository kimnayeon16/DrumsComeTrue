package com.example.drumcomestrue.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.drumcomestrue.api.request.user.LoginRequest;
import com.example.drumcomestrue.api.request.user.SignupRequest;
import com.example.drumcomestrue.api.response.user.FindIdResponse;
import com.example.drumcomestrue.api.response.user.FindPwResponse;
import com.example.drumcomestrue.api.response.user.FindResponse;
import com.example.drumcomestrue.api.response.user.LoginResponse;
import com.example.drumcomestrue.api.response.user.ViewResponse;
import com.example.drumcomestrue.common.error.BadRequestException;
import com.example.drumcomestrue.common.error.DuplicateException;
import com.example.drumcomestrue.common.error.NotFoundException;
import com.example.drumcomestrue.common.exception.ApplicationError;
import com.example.drumcomestrue.common.jwt.service.JwtService;
import com.example.drumcomestrue.db.entity.Role;
import com.example.drumcomestrue.db.entity.User;
import com.example.drumcomestrue.db.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final JwtService jwtService;
	public void signup(SignupRequest signupRequest) {

		check(signupRequest.getUserId());

		User user = User.builder()
			.userId(signupRequest.getUserId())
			.userPw(passwordEncoder.encode(signupRequest.getUserPw()))
			.userName(signupRequest.getUserName())
			.role(Role.USER)
			.build();
		userRepository.save(user);
	}

	public void login(LoginRequest loginRequest) {
		existsUserId(loginRequest.getUserId());
		matchesUserPw(loginRequest);
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

	private void existsUserId(String userId) {
		if(!userRepository.existsByUserId(userId)){
			throw new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
		}
	}

	private void matchesUserPw(LoginRequest loginRequest) {
		User user = userRepository.findByUserId(loginRequest.getUserId()).orElseThrow(() -> new BadRequestException(ApplicationError.MEMBER_NOT_FOUND));
		if(!passwordEncoder.matches(loginRequest.getUserPw(), user.getUserPw())){
			throw new NotFoundException(ApplicationError.USERPW_NOT_MATCH);
		}
	}

	public ViewResponse viewPage(String userPk) {
		long userValue = Long.parseLong(userPk);
		User user = userRepository.findByUserPk(userValue).orElseThrow(()-> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
		ViewResponse viewResponse = ViewResponse.builder()
			.userId(user.getUserId())
			.userName(user.getUserName())
			.build();
		return viewResponse;
	}
}
