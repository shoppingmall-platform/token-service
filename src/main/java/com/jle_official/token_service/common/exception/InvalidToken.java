package com.jle_official.token_service.common.exception;

import com.jle_official.token_service.token.dto.StatusCode;
import org.springframework.http.HttpStatus;

public class InvalidToken extends AbstractApiException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public InvalidToken(){
        super(StatusCode.INVALID.name());
    }
    public InvalidToken(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return STATUS;
    }
}
