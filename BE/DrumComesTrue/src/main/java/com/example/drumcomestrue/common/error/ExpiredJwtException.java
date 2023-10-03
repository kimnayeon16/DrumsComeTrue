package com.example.drumcomestrue.common.error;

import com.example.drumcomestrue.common.exception.ApplicationError;

public class ExpiredJwtException extends JwtException {

    public ExpiredJwtException(ApplicationError error) {
        super(error);
    }
}