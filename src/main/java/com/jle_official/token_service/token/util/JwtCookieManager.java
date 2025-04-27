package com.jle_official.token_service.token.util;

import com.jle_official.token_service.token.dto.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class JwtCookieManager {

    @Value("${jle.jwt.refresh-token.expires-day}")
    private int cookieExpires;

    @Value("${jle.jwt.access-token.name}")
    private String accessTokenName;

    @Value("${jle.jwt.refresh-token.name}")
    private String refreshTokenName;

    public Optional<String> extractTokenFromCookies(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    public void setTokenCookie(HttpServletResponse response, Token token) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(accessTokenName, token.accessToken())
                .path("/")
                .maxAge(Duration.ofDays(cookieExpires))
                .sameSite("Strict")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(refreshTokenName, token.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(cookieExpires))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    public void clearTokenCookie(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenName+"=; Path=/; Max-Age=0; HttpOnly; SameSite=Strict");
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenName+"=; Path=/; Max-Age=0; HttpOnly; SameSite=Strict");
    }
}
