package com.example.drumcomestrue.common.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationError {

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 에러가 발생했습니다."),
	UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "U001", "인증되지 않은 사용자입니다."),
	FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN, "U002", "권한이 없는 사용자입니다."),
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U003", "사용자를 찾을 수 없습니다."),
	DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "U004", "해당 아이디가 이미 존재합니다."),
	DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "U005", "해당 닉네임이 이미 존재합니다."),
	INVALID_COMPANY(HttpStatus.BAD_REQUEST, "U006", "지원하지 않는 회사입니다.")








	;


	private final HttpStatus status;
	private final String code;
	private final String message;

}
