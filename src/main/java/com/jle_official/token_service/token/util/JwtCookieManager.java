package com.jle_official.token_service.token.util;

import com.jle_official.token_service.token.dto.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtCookieManager {

    @Value("${jwt.access-token-expires}")
    private int ACCESS_TOKEN_EXPIRES;
    @Value("${jwt.refresh-token-expires}")
    private int REFRESH_TOKEN_EXPIRES;

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
        response.addHeader("Set-Cookie", "AT=" + token.getAccessToken() + "; Path=/;  Max-Age=" + ACCESS_TOKEN_EXPIRES + "; SameSite=Strict");
        response.addHeader("Set-Cookie", "RT=" + token.getRefreshToken() + "; Path=/; Max-Age=" + REFRESH_TOKEN_EXPIRES + "; HttpOnly; SameSite=Strict");
        response.setStatus(HttpStatus.OK.value());
    }

    public void clearTokenCookie(HttpServletResponse response) {
        response.addHeader("Set-Cookie", "AT=; Path=/; Max-Age=0; HttpOnly; SameSite=Strict");
        response.addHeader("Set-Cookie", "RT=; Path=/; Max-Age=0; HttpOnly; SameSite=Strict");
    }
}
