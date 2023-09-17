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

	/* USER 에러 */
	UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "U001", "인증되지 않은 사용자입니다."),
	FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN, "U002", "권한이 없는 사용자입니다."),
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U003", "사용자를 찾을 수 없습니다."),
	DUPLICATE_USERID(HttpStatus.BAD_REQUEST, "U004", "해당 아이디가 이미 존재합니다."),
	DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "U005", "해당 닉네임이 이미 존재합니다."	),
	LOGIN_ID_IS_FAILED(HttpStatus.BAD_REQUEST,"U006","해당 아이디는 없는 아이디입니다"),
	LOGIN_PW_IS_FAILED(HttpStatus.BAD_REQUEST,"U007","해당 비밀번호는 없는 비밀번호입니다"),




	INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST,"X001", "서버 오류입니다")
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}