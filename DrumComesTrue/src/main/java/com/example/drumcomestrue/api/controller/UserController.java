package com.example.drumcomestrue.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.drumcomestrue.api.request.user.SignupRequest;
import com.example.drumcomestrue.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	@RequestMapping("/signup")
	public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest){
		userService.signup(signupRequest);
		return ResponseEntity.ok().build();
	}

}
