package com.example.drumcomestrue.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.example.drumcomestrue.ApiDocument;
import com.example.drumcomestrue.api.request.user.LoginRequest;
import com.example.drumcomestrue.api.request.user.SignupRequest;
import com.example.drumcomestrue.api.service.UserService;

import com.example.drumcomestrue.common.error.BadRequestException;
import com.example.drumcomestrue.common.exception.ApplicationError;
import com.example.drumcomestrue.common.exception.ApplicationException;
import com.example.drumcomestrue.common.exception.ErrorResponse;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ApiDocument {

	private static final String CONTEXT_PATH = "/api/v1";
	private static final String USER_ID = "아이디";
	private static final String USER_PASSWORD = "비밀번호";
	private static final String USER_NAME = "이름";

	private SignupRequest signupRequest;

	private LoginRequest loginRequest;

	private ApplicationException loginIdNountFoundException;

	private ApplicationException loginPwNountFoundException;


	@MockBean
	private UserService userService;

	public UserControllerTest() {
	}

	@BeforeEach
	void setUp(){
		signupRequest = SignupRequest.builder()
			.userId(USER_ID)
			.userPw(USER_PASSWORD)
			.userName(USER_NAME)
			.build();
		loginRequest = LoginRequest.builder()
			.userId(USER_ID)
			.userPw(USER_PASSWORD)
			.build();



		loginIdNountFoundException = new BadRequestException(ApplicationError.LOGIN_ID_IS_FAILED);
		loginPwNountFoundException = new BadRequestException(ApplicationError.LOGIN_PW_IS_FAILED);
	}


/////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////회원가입/////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
	@Test
	void 회원가입_성공() throws Exception{
		//given
		willDoNothing().given(userService).signup(any(SignupRequest.class));

		//when
		ResultActions resultActions = 회원가입_요청(signupRequest);

		//then
		회원가입_요청_성공(resultActions);
	}

	@Test
	void 회원가입_해당아이디존재_실패() throws Exception{
		//given
		willThrow(new BadRequestException(ApplicationError.DUPLICATE_USERID)).given(userService).signup(any(SignupRequest.class));

		//when
		ResultActions resultActions = 회원가입_요청(signupRequest);

		//then
		회원가입_해당아이디존재_요청_실패(resultActions);
	}

	@Test
	void 회원가입_해당닉네임존재_실패() throws Exception{
		//given
		willThrow(new BadRequestException(ApplicationError.DUPLICATE_USERNAME)).given(userService).signup(any(SignupRequest.class));

		//when
		ResultActions resultActions = 회원가입_요청(signupRequest);

		//then
		회원가입_해당닉네임존재_요청_실패(resultActions);
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

	private void 회원가입_해당아이디존재_요청_실패(ResultActions resultActions) throws Exception {
		resultActions.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(toDocument("signup-id-exist-fail"));
	}

	private void 회원가입_해당닉네임존재_요청_실패(ResultActions resultActions) throws Exception {
		resultActions.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(toDocument("signup-nickname-exist-fail"));
	}
/////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////로그인/////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
	@Test
	void 로그인_성공() throws Exception{
		//given
		willDoNothing().given(userService).login(any(LoginRequest.class));

		//when
		ResultActions resultActions = 로그인_요청();

		//then
		로그인_요청_성공(resultActions);
	}

	@Test
	void 로그인_아이디없음_실패() throws Exception{
		//given
		willThrow(loginIdNountFoundException).given(userService).login(any(LoginRequest.class));

		//when
		ResultActions resultActions = 로그인_요청();

		//then
		로그인_아이디없음_요청_실패(resultActions);
	}

	@Test
	void 로그인_비밀번호매칭_실패() throws Exception{
		//given
		willThrow(loginPwNountFoundException).given(userService).login(any(LoginRequest.class));

		//when
		ResultActions resultActions = 로그인_요청();

		//then
		로그인_비밀번호매칭_요청_실패(resultActions);
	}

	private ResultActions 로그인_요청() throws Exception {
		return mockMvc.perform(post(CONTEXT_PATH+"/user/login")
			.contextPath(CONTEXT_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJson(loginRequest)));
	}

	private void 로그인_요청_성공(ResultActions resultActions) throws Exception {
		printAndMakeSnippet(resultActions
				.andExpect(status().isOk()),
			"login-success");
	}

	private void 로그인_아이디없음_요청_실패(ResultActions resultActions) throws Exception {
		printAndMakeSnippet(resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().json(toJson(ErrorResponse.from(loginIdNountFoundException)))),
			"login-nickname-not-found-fail");
	}

	private void 로그인_비밀번호매칭_요청_실패(ResultActions resultActions) throws Exception {
		printAndMakeSnippet(resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().json(toJson(ErrorResponse.from(loginPwNountFoundException)))),
			"login-password-not-match-fail");
	}


}