package com.jle_official.token_service.token.service.impl;

import com.jle_official.token_service.global.exception.InvalidRefreshToken;
import com.jle_official.token_service.global.exception.LoginFailedException;
import com.jle_official.token_service.member.dto.MemberInfo;
import com.jle_official.token_service.member.service.MemberService;
import com.jle_official.token_service.token.dao.RedisDao;
import com.jle_official.token_service.token.dto.Token;
import com.jle_official.token_service.token.service.TokenService;
import com.jle_official.token_service.token.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final RedisDao redisDao;
    private final MemberService memberService;

    @Override
    public Token issueJwt(String loginId, String password) {
        if (loginId == null || loginId.isEmpty() || password == null || password.isEmpty()) {
            throw new LoginFailedException(loginId + ": id or password is empty");
        }

        if (memberService.validateLogin(loginId, password)) {
            MemberInfo memberInfo = memberService.getMemberInfo(loginId);

            return issueToken(String.valueOf(memberInfo.getMemberId()));

        } else {
            throw new LoginFailedException(loginId + ": invalid password");
        }
    }

    @Override
    public Token reissueToken(String accessToken, String refreshToken) {
        String memberId = JwtUtils.extractMemberId(accessToken);
        String storedToken = redisDao.getToken(memberId);

        if (JwtUtils.validateToken(refreshToken, storedToken)) {
            blackListToken(accessToken, refreshToken);
            return issueToken(memberId);
        } else {
            throw new InvalidRefreshToken();
        }
    }

    private void blackListToken(String accessToken, String refreshToken) {
        redisDao.saveToken(accessToken, "", JwtUtils.extractExpirationTime(accessToken));
        redisDao.deleteToken(JwtUtils.extractMemberId(refreshToken));
    }

    private Token issueToken(String id) {
        String accessToken = JwtUtils.generateAccessToken(id);
        String refreshToken = JwtUtils.generateRefreshToken(id);
        Duration expiration = JwtUtils.extractExpirationTime(refreshToken);

        redisDao.saveToken(id, refreshToken, expiration);
        return new Token(accessToken, refreshToken);
    }


}
