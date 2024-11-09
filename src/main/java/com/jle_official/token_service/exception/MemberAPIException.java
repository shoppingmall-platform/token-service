package com.jle_official.token_service.exception;

import com.jle_official.token_service.service.dto.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MemberAPIException extends AbstractApiException {
    private final HttpStatus status;

    public MemberAPIException(String message, ResponseEntity<?> response) {
        super("member api 오류 : " + message);
        this.status = HttpStatus.valueOf(response.getStatusCode().value());
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }
}
