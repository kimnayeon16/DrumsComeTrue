package com.example.drumcomestrue.common.error;

import com.example.drumcomestrue.common.exception.ApplicationError;
import com.example.drumcomestrue.common.exception.ApplicationException;

public class JwtException extends ApplicationException {
	public JwtException(ApplicationError error) {
		super(error);
	}
}
