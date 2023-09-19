package com.example.drumcomestrue.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{

	private final HttpStatus status;
	private final String code;

	public ApplicationException(ApplicationError error){
		super(error.getMessage());
		this.status=error.getHttpStatus();
		this.code = error.getCode();
	}
}
