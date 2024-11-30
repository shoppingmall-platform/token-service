package com.jle_official.token_service.token.controller;

import com.jle_official.token_service.token.controller.dto.LoginRequest;
import com.jle_official.token_service.token.controller.dto.LoginResponse;
import com.jle_official.token_service.token.dto.Token;
import com.jle_official.token_service.token.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<Token> refresh(@RequestHeader("Authorization") String authorization, @CookieValue String refreshToken) {
        if (authorization == null || refreshToken == null || !authorization.startsWith("Bearer ") || authorization.length() < 8) {
            return ResponseEntity.badRequest().build();
        }

        String accessToken = authorization.substring(7);
        return ResponseEntity.ok(tokenService.reissueToken(accessToken, refreshToken));
    }

}
