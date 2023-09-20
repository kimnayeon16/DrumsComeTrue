package com.example.drumcomestrue.common.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.example.drumcomestrue.common.exception.ApplicationError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final String CHARACTER_ENCODING = "UTF-8";
	private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setCharacterEncoding(CHARACTER_ENCODING);
		response.setContentType(CONTENT_TYPE);
		response.getWriter().write(ApplicationError.NOT_LOGIN_ERROR.getMessage());
		log.info("로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
	}
}