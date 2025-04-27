package com.jle_official.token_service.common.exception;

import org.springframework.http.HttpStatus;

public class LogoutException extends AbstractApiException {
    private final HttpStatus status;

    public LogoutException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }
}
