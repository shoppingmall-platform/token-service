package com.jle_official.token_service.global.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshToken extends AbstractApiException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public InvalidRefreshToken(){
        super();
    }
    public InvalidRefreshToken(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return STATUS;
    }
}
