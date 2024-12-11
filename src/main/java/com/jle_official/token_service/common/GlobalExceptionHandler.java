package com.jle_official.token_service.common;

import com.jle_official.token_service.common.exception.AbstractApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AbstractApiException.class)
    public ResponseEntity<String> apiExceptionHandler(AbstractApiException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus
    public ResponseEntity<String> unknownExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
