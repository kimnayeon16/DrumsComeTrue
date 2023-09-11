package com.example.drumcomestrue.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(ApplicationException.class)
	protected ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
		log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		return ResponseEntity.status(exception.getStatus()).body(ErrorResponse.from(exception));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
		log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		return ResponseEntity.internalServerError().body(ErrorResponse.create());
	}
}