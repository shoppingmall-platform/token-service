package com.jle_official.token_service.token.service;

import com.jle_official.token_service.member.dto.MemberInfo;
import com.jle_official.token_service.token.dto.Token;

public interface TokenService {
    Token issueJwt(MemberInfo memberInfo);

    Token reissueToken(String accessToken);

    void deleteToken(String accessToken);

}
