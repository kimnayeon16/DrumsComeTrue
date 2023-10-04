package com.example.drumcomestrue.common.error;

import com.example.drumcomestrue.common.exception.ApplicationError;
import com.example.drumcomestrue.common.exception.ApplicationException;

public class UnAuthorizedException extends ApplicationException {
	public UnAuthorizedException(ApplicationError error) {
		super(error);
	}
}
