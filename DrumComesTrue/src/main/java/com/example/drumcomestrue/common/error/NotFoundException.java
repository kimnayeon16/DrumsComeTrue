package com.example.drumcomestrue.common.error;

import com.example.drumcomestrue.common.exception.ApplicationError;
import com.example.drumcomestrue.common.exception.ApplicationException;

public class NotFoundException extends ApplicationException {

	public NotFoundException(ApplicationError error) {
		super(error);
	}
}
