package com.jle_official.token_service.token.filter;

import com.jle_official.token_service.common.exception.LoginFailedException;
import com.jle_official.token_service.common.security.PrincipalDetails;
import com.jle_official.token_service.token.dto.Token;
import com.jle_official.token_service.token.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null) {
            throw new LoginFailedException("Invalid username or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.info("[attemptAuthentication] username= {}, password= {}", username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Token token = tokenService.issueJwt(principalDetails.getMemberInfo());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Access-Token", token.getAccessToken());
        response.setHeader("Refresh-Token", token.getRefreshToken());

        log.info("[successfulAuthentication]\n    access token= {}\n    password= {}", token.getAccessToken(), token.getRefreshToken());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // TODO 로그인 실패 로직
        log.info("[unsuccessfulAuthentication] username= {}", request.getRemoteUser());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("login failed");
    }
}
