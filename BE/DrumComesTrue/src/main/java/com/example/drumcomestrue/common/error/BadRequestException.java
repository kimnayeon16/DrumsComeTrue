package com.example.drumcomestrue.common.error;

import com.example.drumcomestrue.common.exception.ApplicationError;
import com.example.drumcomestrue.common.exception.ApplicationException;

public class BadRequestException extends ApplicationException {
	public BadRequestException(ApplicationError error) {
		super(error);
	}
}
