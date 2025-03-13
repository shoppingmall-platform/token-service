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
@RequestMapping("/v1/token")
public class TokenController {
    private final TokenService tokenService;
    private final JwtCookieManager jwtCookieManager;

    @PostMapping("/refresh")
    public ResponseEntity<Token> refresh(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtCookieManager.extractTokenFromCookies(request, "AT").orElseThrow(()-> new InvalidToken("access token 을 보내주세요"));
        String refreshToken = jwtCookieManager.extractTokenFromCookies(request, "RT").orElseThrow(()-> new InvalidToken("refresh token 을 보내주세요"));

        log.debug("[AT] : {}", accessToken);
        log.debug("[RT]: {}", refreshToken);

        Token newToken = tokenService.reissueToken(accessToken, refreshToken);
        jwtCookieManager.setTokenCookie(response, newToken);

        return ResponseEntity.ok(newToken);
    }

}
