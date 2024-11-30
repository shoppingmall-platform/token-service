package com.jle_official.token_service.common.security;

import com.jle_official.token_service.common.exception.LogoutFailedException;
import com.jle_official.token_service.token.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {
    private final TokenService tokenService;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String accessToken = request.getHeader("Authorization").substring(7);

            tokenService.blackListToken(accessToken, "logout");

        } catch (NullPointerException e) {
            throw new LogoutFailedException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
