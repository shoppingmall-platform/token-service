package com.jle_official.token_service.token.controller;

import com.jle_official.token_service.common.exception.InvalidToken;
import com.jle_official.token_service.token.dto.Token;
import com.jle_official.token_service.token.service.TokenService;
import com.jle_official.token_service.token.util.JwtCookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TokenController는 JWT 토큰 재발급을 처리하는 REST 컨트롤러입니다.
 *
 * 클라이언트가 전송한 access token과 refresh token을 쿠키에서 추출하여
 * 새로운 access token을 발급하고, 응답 쿠키에 설정합니다.
 *
 * 토큰은 {@link JwtCookieManager}를 통해 쿠키에서 읽고 쓸 수 있으며,
 * 재발급 로직은 {@link TokenService}에서 처리됩니다.
 *
 * @author JLE
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {

    /** 토큰 재발급 서비스 */
    private final TokenService tokenService;

    /** JWT 쿠키 처리 유틸리티 */
    private final JwtCookieManager jwtCookieManager;

    /** Access Token 쿠키 이름 (application.yml에서 주입) */
    @Value("${jle.jwt.access-token.name}")
    private String accessTokenName;

    /** Refresh Token 쿠키 이름 (application.yml에서 주입) */
    @Value("${jle.jwt.refresh-token.name}")
    private String refreshTokenName;

    /**
     * Access Token이 만료되었을 경우, refresh token을 이용하여 새로운 access token을 발급합니다.
     *
     * <p>요청에서 쿠키를 통해 access token과 refresh token을 추출하고,
     * 유효성을 검증한 뒤 새 토큰을 재발급하여 응답 쿠키에 저장합니다.
     *
     * @param request  클라이언트의 HTTP 요청 (쿠키 포함)
     * @param response 클라이언트에 전달할 HTTP 응답 (쿠키 설정 목적)
     * @return 새로 발급된 access token (본문에 포함)
     * @throws InvalidToken access token 또는 refresh token이 누락되었을 경우
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtCookieManager.extractTokenFromCookies(request, accessTokenName)
                .orElseThrow(() -> new InvalidToken("access token is null"));
        String refreshToken = jwtCookieManager.extractTokenFromCookies(request, refreshTokenName)
                .orElseThrow(() -> new InvalidToken("refresh token is null"));

        log.debug("[at] : {}", accessToken);
        log.debug("[rt]: {}", refreshToken);

        Token newToken = tokenService.reissueToken(accessToken, refreshToken);
        jwtCookieManager.setTokenCookie(response, newToken);

        return ResponseEntity.ok(newToken.accessToken());
    }
}
