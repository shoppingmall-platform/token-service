package com.jle_official.token_service.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Token {
    private final String accessToken;
    private final String refreshToken;
}
