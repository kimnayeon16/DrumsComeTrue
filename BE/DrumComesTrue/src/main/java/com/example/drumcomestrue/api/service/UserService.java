package com.example.drumcomestrue.api.service;

import org.springframework.stereotype.Service;

import com.example.drumcomestrue.api.request.user.LoginRequest;
import com.example.drumcomestrue.api.request.user.SignupRequest;
import com.example.drumcomestrue.api.response.user.FindIdResponse;
import com.example.drumcomestrue.api.response.user.FindPwResponse;
import com.example.drumcomestrue.api.response.user.FindResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	public void signup(SignupRequest signupRequest) {
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
}
