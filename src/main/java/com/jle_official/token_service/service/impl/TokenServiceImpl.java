package com.jle_official.token_service.service.impl;

import com.jle_official.token_service.domain.Token;
import com.jle_official.token_service.exception.PasswordNotMatchException;
import com.jle_official.token_service.service.MemberService;
import com.jle_official.token_service.service.TokenService;
import com.jle_official.token_service.service.dto.MemberInfo;
import com.jle_official.token_service.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final MemberService memberService;

    @Override
    public Token issueToken(String loginId, String password) {
        if (memberService.validateLogin(loginId, password)) {
            MemberInfo memberInfo = memberService.getMemberInfo(loginId);
            String accessToken = JwtUtils.generateAccessToken(memberInfo);
            String refreshToken = JwtUtils.generateRefreshToken(memberInfo);
            return new Token(accessToken, refreshToken);
        } else {
            throw new PasswordNotMatchException(loginId);
        }
    }

    @Override
    public String reIssueToken(String refreshToken) {
        return "";
    }

}
