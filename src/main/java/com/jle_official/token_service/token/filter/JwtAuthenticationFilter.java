package com.jle_official.token_service.token.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jle_official.token_service.common.exception.LoginFailedException;
import com.jle_official.token_service.common.security.PrincipalDetails;
import com.jle_official.token_service.token.dto.Token;
import com.jle_official.token_service.token.service.TokenService;
import com.jle_official.token_service.token.util.JwtCookieManager;
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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final JwtCookieManager jwtCookieManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username;
        String password;

        try {
            // JSON 형식 요청일 경우
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
                username = requestBody.get("loginId");
                password = requestBody.get("password");
            } else {
                // Form 형식 요청일 경우
                username = request.getParameter("loginId");
                password = request.getParameter("password");
            }

            if (username == null || password == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email or password");
                throw new LoginFailedException("Invalid loginId or password");
            }
        } catch (IOException e) {
            throw new LoginFailedException("Invalid loginId or password");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.debug("[attemptAuthentication] username= {}, password= {}", username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        log.debug("[login success] : {}", principalDetails.getUsername());
        Token token = tokenService.issueJwt(principalDetails.getMemberInfo());

        jwtCookieManager.setTokenCookie(response, token);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        log.debug("[successfulAuthentication]");
        log.debug("- access token= {}", token.accessToken());
        log.debug("- refresh token= {}", token.refreshToken());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.debug("[unsuccessfulAuthentication] username= {}", request.getRemoteUser());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("login failed");
    }
}
