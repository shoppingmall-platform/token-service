package com.jle_official.token_service.token.controller;

import com.jle_official.token_service.common.exception.InvalidToken;
import com.jle_official.token_service.token.dto.Token;
import com.jle_official.token_service.token.service.TokenService;
import com.jle_official.token_service.token.util.JwtCookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;
    private final JwtCookieManager jwtCookieManager;

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtCookieManager.extractTokenFromCookies(request, "at").orElseThrow(()-> new InvalidToken("access token is null"));
        String refreshToken = jwtCookieManager.extractTokenFromCookies(request, "rt").orElseThrow(()-> new InvalidToken("refresh token is null"));

        log.debug("[at] : {}", accessToken);
        log.debug("[rt]: {}", refreshToken);

        Token newToken = tokenService.reissueToken(accessToken, refreshToken);
        jwtCookieManager.setTokenCookie(response, newToken);

        return ResponseEntity.ok(newToken.accessToken());
    }

}
