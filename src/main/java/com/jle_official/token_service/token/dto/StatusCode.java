package com.jle_official.token_service.token.dto;

import org.springframework.http.HttpStatus;

public enum StatusCode {
    VALID(HttpStatus.OK),
    INVALID(HttpStatus.UNAUTHORIZED),
    EXPIRED(HttpStatus.UNAUTHORIZED);

    private final HttpStatus httpStatus;

    StatusCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
