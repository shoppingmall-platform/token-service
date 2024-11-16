package com.jle_official.token_service.token.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
