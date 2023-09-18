package com.example.drumcomestrue.common.error;

import com.example.drumcomestrue.common.exception.ApplicationError;
import com.example.drumcomestrue.common.exception.ApplicationException;

public class DuplicateException extends ApplicationException {
	public DuplicateException(ApplicationError error) {
		super(error);
	}
}
