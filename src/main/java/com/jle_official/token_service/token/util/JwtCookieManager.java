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

    @Value("${jle.jwt.refresh-token.expires}")
    private Duration cookieExpires;

    @Value("${jle.jwt.access-token.name}")
    private String accessTokenName;


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
//                .httpOnly(true)
                .sameSite("Strict")
                .maxAge(cookieExpires)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }

    public void clearTokenCookie(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(accessTokenName, "")
                .path("/")
                .httpOnly(true)
                .sameSite("Strict")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }
}
