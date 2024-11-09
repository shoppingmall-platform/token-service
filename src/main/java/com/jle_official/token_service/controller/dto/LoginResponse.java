package com.jle_official.token_service.controller.dto;

import com.jle_official.token_service.domain.Token;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final Token token;

    public LoginResponse(Token token) {
        this.token = token;
    }
}
