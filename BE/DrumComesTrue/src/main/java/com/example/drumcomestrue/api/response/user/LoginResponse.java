package com.example.drumcomestrue.api.response.user;

import com.example.drumcomestrue.api.request.user.LoginRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {
	String RefreshToken;
	String AccessToken;

}
