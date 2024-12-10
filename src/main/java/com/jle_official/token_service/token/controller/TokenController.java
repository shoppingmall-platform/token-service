package com.jle_official.token_service.token.controller;

import com.jle_official.token_service.token.dto.Token;
import com.jle_official.token_service.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/token")
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
