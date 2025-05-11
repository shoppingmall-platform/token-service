package com.jle_official.token_service.common.exception;

import com.jle_official.token_service.token.dto.StatusCode;
import org.springframework.http.HttpStatus;

public class ExpiredToken extends AbstractApiException {

    public ExpiredToken(){
        super(StatusCode.EXPIRED.name());
    }
    public ExpiredToken(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
