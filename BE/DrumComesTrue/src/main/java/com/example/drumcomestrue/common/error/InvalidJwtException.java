package com.example.drumcomestrue.common.error;

import com.example.drumcomestrue.common.exception.ApplicationError;

public class InvalidJwtException extends JwtException {

    public InvalidJwtException(ApplicationError error) {
        super(error);
    }
}