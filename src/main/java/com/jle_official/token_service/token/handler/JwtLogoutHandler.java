package com.jle_official.token_service.token.handler;

import com.jle_official.token_service.common.exception.InvalidToken;
import com.jle_official.token_service.common.exception.LogoutException;
import com.jle_official.token_service.token.service.TokenService;
import com.jle_official.token_service.token.util.JwtCookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {
    private final TokenService tokenService;
    private final JwtCookieManager jwtCookieManager;

    @Value("${jle.jwt.access-token.name}")
    private String accessTokenName;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = jwtCookieManager.extractTokenFromCookies(request, accessTokenName).orElseThrow(()->new InvalidToken());
        try {
            tokenService.deleteToken(accessToken);
            jwtCookieManager.clearTokenCookie(response);
        } catch (Exception e) {
            throw new LogoutException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
