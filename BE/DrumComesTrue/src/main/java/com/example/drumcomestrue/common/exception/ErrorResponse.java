package com.example.drumcomestrue.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

	private final String code;
	private final String message;

	public static ErrorResponse create() {
		return new ErrorResponse(ApplicationError.INTERNAL_SERVER_ERROR.getCode(),
			ApplicationError.INTERNAL_SERVER_ERROR.getMessage());
	}

	public static ErrorResponse from(ApplicationException exception) {
		return new ErrorResponse(exception.getCode(), exception.getMessage());
	}
}