package com.jle_official.token_service.token.service;

import com.jle_official.token_service.common.security.PrincipalDetails;
import com.jle_official.token_service.token.dto.Token;

public interface TokenService {
    Token issueJwt(PrincipalDetails principalDetails);

    Token reissueToken(String accessToken, String refreshToken);

    void blackListToken(String accessToken, String type);

}
