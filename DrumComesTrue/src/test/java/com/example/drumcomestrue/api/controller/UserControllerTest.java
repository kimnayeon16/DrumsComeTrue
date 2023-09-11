package com.example.drumcomestrue.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.example.drumcomestrue.ApiDocument;
import com.example.drumcomestrue.api.request.user.SignupRequest;
import com.example.drumcomestrue.api.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ApiDocument {

	private static final String CONTEXT_PATH = "/api/v1";
	private static final String USER_ID = "아이디";
	private static final String USER_PASSWORD = "비밀번호";
	private static final String USER_NAME = "이름";

	private SignupRequest signupRequest;

	@MockBean
	private UserService userService;

	@BeforeEach
	void setUp(){
		signupRequest = SignupRequest.builder()
			.userId(USER_ID)
			.userPw(USER_PASSWORD)
			.userName(USER_NAME)
			.build();
	}

	@Test
	void 회원가입_성공() throws Exception{
		//given
		willDoNothing().given(userService).signup(any(SignupRequest.class));

		//when
		ResultActions resultActions = 회원가입_요청(signupRequest);

		//then
		회원가입_요청_성공(resultActions);
	}

	private ResultActions 회원가입_요청(SignupRequest signupRequest) throws Exception {
		return mockMvc.perform(post(CONTEXT_PATH+"/user/signup")
			.contextPath(CONTEXT_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJson(signupRequest)));
	}

	private void 회원가입_요청_성공(ResultActions resultActions) throws Exception {
		resultActions.andExpect(status().isOk())
			.andDo(print())
			.andDo(toDocument("signup-success"));
	}


}