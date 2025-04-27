package com.jle_official.token_service.common;

import com.jle_official.token_service.common.exception.AbstractApiException;
import com.jle_official.token_service.common.exception.InvalidToken;
import com.jle_official.token_service.common.exception.LogoutException;
import com.jle_official.token_service.token.util.JwtCookieManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final JwtCookieManager jwtCookieManager;

    @ExceptionHandler(AbstractApiException.class)
    public ResponseEntity<String> apiExceptionHandler(AbstractApiException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler({InvalidToken.class, LogoutException.class})
    public ResponseEntity<String> tokenExceptionHandler(AbstractApiException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void unknownExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
    }
}
