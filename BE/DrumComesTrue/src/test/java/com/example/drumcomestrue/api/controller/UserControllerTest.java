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
import com.example.drumcomestrue.api.response.user.FindIdResponse;
import com.example.drumcomestrue.api.response.user.FindPwResponse;
import com.example.drumcomestrue.api.response.user.FindResponse;
import com.example.drumcomestrue.api.service.UserService;

import com.example.drumcomestrue.common.error.BadRequestException;
import com.example.drumcomestrue.common.error.NotFoundException;
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

	private static final String USER_TEMP_PASSWORD = "임시_비밀번호";
	private static final String USER_NAME = "이름";
	private static final String USER_NUMBER = "010-9999-9999";

	private static final String VERIFY_NUMBER = "SD3747FF";

	private SignupRequest signupRequest;

	private LoginRequest loginRequest;

	private FindResponse findResponse;

	private FindIdResponse findIdResponse;

	private FindPwResponse findPwResponse;

	private ApplicationException loginIdNotFoundException;

	private ApplicationException loginPwNotFoundException;

	private ApplicationException phoneNotFoundFoundException;


	@MockBean
	private UserService userService;

	@BeforeEach
	void setUp(){
		signupRequest = SignupRequest.builder()
			.userId(USER_ID)
			.userPw(USER_PASSWORD)
			.userName(USER_NAME)
			.userNumber(USER_NUMBER)
			.build();
		loginRequest = LoginRequest.builder()
			.userId(USER_ID)
			.userPw(USER_PASSWORD)
			.build();

		findResponse = FindResponse.builder()
			.verifyNumber("SJDFJ34")
			.build();

		findIdResponse = FindIdResponse.builder()
			.userId(USER_ID)
			.build();

		findPwResponse = FindPwResponse.builder()
			.userPw(USER_TEMP_PASSWORD)
			.build();

		loginIdNotFoundException = new BadRequestException(ApplicationError.LOGIN_ID_IS_FAILED);
		loginPwNotFoundException = new BadRequestException(ApplicationError.LOGIN_PW_IS_FAILED);
		phoneNotFoundFoundException = new NotFoundException(ApplicationError.NOTFOUND_PHONE_NUMBER);
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
		willThrow(loginIdNotFoundException).given(userService).login(any(LoginRequest.class));

		//when
		ResultActions resultActions = 로그인_요청();

		//then
		로그인_아이디없음_요청_실패(resultActions);
	}

	@Test
	void 로그인_비밀번호매칭_실패() throws Exception{
		//given
		willThrow(loginPwNotFoundException).given(userService).login(any(LoginRequest.class));

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
				.andExpect(content().json(toJson(ErrorResponse.from(loginIdNotFoundException)))),
			"login-nickname-not-found-fail");
	}

	private void 로그인_비밀번호매칭_요청_실패(ResultActions resultActions) throws Exception {
		printAndMakeSnippet(resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().json(toJson(ErrorResponse.from(loginPwNotFoundException)))),
			"login-password-not-match-fail");
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////문자메세지 발송//////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	void 전화번호_발송_성공() throws Exception{
		//given
		willReturn(findResponse).given(userService).verify(anyString());

		//when
		ResultActions resultActions = 전화번호_발송_요청();

		//then
		전화번호_발송_요청_성공(resultActions);
	}

	@Test
	void 전화번호_발송_실패() throws Exception{
		//given
		willThrow(phoneNotFoundFoundException).given(userService).verify(anyString());

		//when
		ResultActions resultActions = 전화번호_발송_요청();

		//then
		전화번호_발송_요청_실패(resultActions);
	}

	private ResultActions 전화번호_발송_요청() throws Exception {
		return mockMvc.perform(get(CONTEXT_PATH+"/user/verify/{phoneNumber}",USER_NUMBER)
			.contextPath(CONTEXT_PATH)
			.contentType(MediaType.APPLICATION_JSON));
	}


	private void 전화번호_발송_요청_성공(ResultActions resultActions) throws Exception {
		printAndMakeSnippet(resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(toJson(findResponse))),
			"phone-number-send-success");
	}

	private void 전화번호_발송_요청_실패(ResultActions resultActions) throws Exception {
		printAndMakeSnippet(resultActions
				.andExpect(status().isNotFound())
				.andExpect(content().json(toJson(ErrorResponse.from(phoneNotFoundFoundException)))),
			"phone-number-send-fail");

	}
	/////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////아이디 찾기//////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	@Test
	void 아이디찾기_성공() throws Exception{
		//given
		willReturn(findIdResponse).given(userService).findId(anyString());

		//when
		ResultActions resultActions = 아이디찾기_요청();
		//then
		아이디찾기_요청_성공(resultActions);
	}

	@Test
	void 아이디찾기_실패() throws Exception{
		//given
		willThrow(loginIdNotFoundException).given(userService).findId(anyString());

		//when
		ResultActions resultActions = 아이디찾기_요청();

		//then
		아이디찾기_요청_실패(resultActions);
	}

	private ResultActions 아이디찾기_요청() throws Exception {
		return mockMvc.perform(get(CONTEXT_PATH+"/user/findId/{verifyNumber}",VERIFY_NUMBER)
			.contextPath(CONTEXT_PATH)
			.contentType(MediaType.APPLICATION_JSON));
	}

	private void 아이디찾기_요청_성공(ResultActions resultActions) throws Exception {
		printAndMakeSnippet(resultActions
					.andExpect(status().isOk())
					.andExpect(content().json(toJson(findIdResponse))),
			"id-find-user-success");
	}

	private void 아이디찾기_요청_실패(ResultActions resultActions) throws Exception{
		printAndMakeSnippet(resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().json(toJson(ErrorResponse.from(loginIdNotFoundException)))),
			"id-find-user-fail");
	}
	/////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////비밀번호 찾기 /////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	void 비밀번호찾기_성공() throws Exception{
		//given
		willReturn(findPwResponse).given(userService).findPw(anyString());

		//when
		ResultActions resultActions = 비밀번호찾기_요청();

		//then
		비밀번호찾기_요청_성공(resultActions);
	}
	@Test
	void 비밀번호찾기_실패() throws Exception{
		//given
		willThrow(loginIdNotFoundException).given(userService).findPw(anyString());

		//when
		ResultActions resultActions = 비밀번호찾기_요청();

		//then
		비밀번호찾기_요청_실패(resultActions);
	}

	private ResultActions 비밀번호찾기_요청() throws Exception {
		return mockMvc.perform(get(CONTEXT_PATH+"/user/findPw/{verifyNumber}",VERIFY_NUMBER)
			.contextPath(CONTEXT_PATH)
			.contentType(MediaType.APPLICATION_JSON));
	}

	private void 비밀번호찾기_요청_성공(ResultActions resultActions) throws Exception {
		printAndMakeSnippet(resultActions
				.andExpect(status().isOk())
				.andExpect(content().json(toJson(findPwResponse))),
			"pw-find-user-success");
	}

	private void 비밀번호찾기_요청_실패(ResultActions resultActions) throws Exception{
		printAndMakeSnippet(resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().json(toJson(ErrorResponse.from(loginIdNotFoundException)))),
			"pw-find-user-fail");
	}
}