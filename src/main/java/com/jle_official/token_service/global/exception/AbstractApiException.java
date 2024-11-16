package com.jle_official.token_service.global.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractApiException extends RuntimeException {
    protected AbstractApiException() {
        super();
    }

    protected AbstractApiException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
