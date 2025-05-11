package com.jle_official.token_service.token.controller;

import com.jle_official.token_service.common.exception.InvalidToken;
import com.jle_official.token_service.token.dto.StatusCode;
import com.jle_official.token_service.token.dto.Token;
import com.jle_official.token_service.token.service.TokenService;
import com.jle_official.token_service.token.util.JwtCookieManager;
import com.jle_official.token_service.token.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 토큰 검증 및 재발급을 처리하는 컨트롤러.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class TokenController {
    private final JwtCookieManager jwtCookieManager;
    private final TokenService tokenService;
    private final JwtUtils jwtUtils;

    @Value("${jle.jwt.access-token.name}")
    private String accessTokenName;

    /**
     * 클라이언트의 Access Token 유효성을 검사합니다.
     *
     * @param request HTTP 요청 (쿠키에서 Access Token 추출)
     * @return 토큰 상태 (VALID, EXPIRED, INVALID)에 따른 HTTP 응답
     * @throws InvalidToken 토큰이 존재하지 않거나 비어 있을 경우
     */
    @GetMapping("/check-token")
    public ResponseEntity<String> checkToken(HttpServletRequest request) {
        String accessToken = jwtCookieManager.extractTokenFromCookies(request, accessTokenName).orElseThrow(()->new InvalidToken());
        StatusCode statusCode = jwtUtils.validateToken(accessToken);

        return ResponseEntity.status(statusCode.getHttpStatus()).body(statusCode.name());
    }


    /**
     * Refresh Token을 이용해 새로운 Access/Refresh Token을 발급하고,
     * 쿠키에 저장합니다.
     *
     * @param request  HTTP 요청 (Access Token 포함된 쿠키)
     * @param response HTTP 응답 (새 토큰을 쿠키에 설정)
     * @return 새로 발급된 Access Token
     * @throws InvalidToken Access Token이 존재하지 않거나 비어 있을 경우
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtCookieManager.extractTokenFromCookies(request, accessTokenName).orElseThrow(()->new InvalidToken());
        log.debug("[at] : {}", accessToken);

        Token newToken = tokenService.reissueToken(accessToken);
        jwtCookieManager.setTokenCookie(response, newToken);

        return ResponseEntity.ok().body(newToken.accessToken());
    }
}
