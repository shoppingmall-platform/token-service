package com.jle_official.token_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Token {
    private final String accessToken;
    private final String refreshToken;
}
