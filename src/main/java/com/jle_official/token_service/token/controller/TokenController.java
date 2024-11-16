package com.jle_official.token_service.token.controller;

import com.jle_official.token_service.token.controller.dto.LoginRequest;
import com.jle_official.token_service.token.controller.dto.LoginResponse;
import com.jle_official.token_service.token.dto.Token;
import com.jle_official.token_service.token.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tokens")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        String loginId = request.getLoginId();
        String password = request.getPassword();

        Token token = tokenService.issueJwt(loginId, password);
        return new LoginResponse(token);
    }

}
