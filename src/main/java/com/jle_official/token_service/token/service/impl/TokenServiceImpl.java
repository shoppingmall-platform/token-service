package com.jle_official.token_service.token.service.impl;

import com.jle_official.token_service.common.exception.ExpiredToken;
import com.jle_official.token_service.common.exception.InvalidToken;
import com.jle_official.token_service.member.adaptor.MemberAdapter;
import com.jle_official.token_service.member.dto.MemberInfo;
import com.jle_official.token_service.token.dao.RedisDao;
import com.jle_official.token_service.token.dto.StatusCode;
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
    private final JwtUtils jwtUtils;
    private final MemberAdapter memberAdapter;

    @Override
    public Token issueJwt(MemberInfo memberInfo) {
        String accessToken = jwtUtils.generateAccessToken(memberInfo);
        String refreshToken = jwtUtils.generateRefreshToken(memberInfo.getMemberId());
        Duration expiration = jwtUtils.extractExpirationTime(refreshToken);

        redisDao.saveToken(accessToken, refreshToken, expiration);
        return new Token(accessToken, refreshToken);
    }

    @Override
    public Token reissueToken(String accessToken) {
        String refreshToken = redisDao.getToken(accessToken);
        StatusCode refreshTokenStatusCode = jwtUtils.validateToken(refreshToken);
        if (StatusCode.VALID.equals(refreshTokenStatusCode)) {

            String memberId = jwtUtils.extractMemberId(refreshToken);
            MemberInfo memberInfo = memberAdapter.getMemberInfo(memberId);

            deleteToken(accessToken);

            return issueJwt(memberInfo);
        } else if (StatusCode.EXPIRED.equals(refreshTokenStatusCode)) {
            throw new ExpiredToken();
        } else {
            throw new InvalidToken();
        }
    }

    @Override
    public void deleteToken(String accessToken) {
        redisDao.deleteToken(accessToken);
    }


}
