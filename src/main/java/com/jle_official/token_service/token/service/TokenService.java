package com.jle_official.token_service.token.service;

import com.jle_official.token_service.token.dto.Token;

public interface TokenService {
    Token issueJwt(String loginId, String password);

    Token reissueToken(String accessToken, String refreshToken);

    void blackListToken(String accessToken);

}
