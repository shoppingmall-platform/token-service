package com.jle_official.token_service.common.exception;

import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends AbstractApiException {
    public PasswordNotMatchException(String message) {
        super("비밀번호 오류. id:" + message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
