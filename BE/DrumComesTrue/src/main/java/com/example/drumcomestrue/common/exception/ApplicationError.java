package com.example.drumcomestrue.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationError {

	/* 400 BAD_REQUEST : 잘못된 요청 */
	/* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
	/* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
	/* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */

	/* 인증 에러 */
	UNAUTHORIZED_RTExpiredException(HttpStatus.UNAUTHORIZED, "R001", "Refresh Token이 만료되었습니다. 다시 로그인을 진행하여 Token을 갱신해주세요."),
	UNAUTHORIZED_RTVerifyException(HttpStatus.BAD_REQUEST, "R002", "유효하지 않은 Refresh Token 입니다."),

	/* USER 에러 */
	UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "U001", "인증되지 않은 사용자입니다."),
	FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN, "U002", "권한이 없는 사용자입니다."),
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U003", "사용자를 찾을 수 없습니다."),
	DUPLICATE_USERID(HttpStatus.BAD_REQUEST, "U004", "해당 아이디가 이미 존재합니다."),
	DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "U005", "해당 닉네임이 이미 존재합니다."	),
	LOGIN_ID_IS_FAILED(HttpStatus.BAD_REQUEST,"U006","해당 아이디는 없는 아이디입니다"),
	LOGIN_PW_IS_FAILED(HttpStatus.BAD_REQUEST,"U007","해당 비밀번호는 없는 비밀번호입니다"),
	NOTFOUND_PHONE_NUMBER(HttpStatus.NOT_FOUND,"U007","해당 핸드폰번호는 없는 번호입니다"),
	NOT_EQUAL_ID_OR_PASSWORD(BAD_REQUEST,"U008","아이디 또는 비밀번호가 일치하지 않습니다"),
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, "U009", "token이 유효하지 않습니다."),
	EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "U010", "token 유효기간이 만료되었습니다."),
	USERPW_NOT_MATCH(HttpStatus.BAD_REQUEST, "U11", "비밀번호가 올바르지 않습니다."),
	NOT_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "U12", "로그인에 실패했습니다."),

	INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST,"X001", "서버 오류입니다")
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}