package com.jle_official.token_service.token.service.impl;

import com.jle_official.token_service.common.exception.InvalidRefreshToken;
import com.jle_official.token_service.member.adaptor.MemberAdapter;
import com.jle_official.token_service.member.dto.MemberInfo;
import com.jle_official.token_service.common.security.PrincipalDetails;
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
    private final JwtUtils jwtUtils;
    private final MemberAdapter memberAdapter;

    @Override
    public Token issueJwt(MemberInfo memberInfo) {
        String accessToken = jwtUtils.generateAccessToken(memberInfo);
        String refreshToken = jwtUtils.generateRefreshToken(memberInfo.getId());
        Duration expiration = jwtUtils.extractExpirationTime(refreshToken);

        redisDao.saveToken(memberInfo.getId(), refreshToken, expiration);
        return new Token(accessToken, refreshToken);
    }

    @Override
    public Token reissueToken(String accessToken, String refreshToken) {
        String memberId = jwtUtils.extractMemberId(accessToken);
        String storedToken = redisDao.getToken(memberId);

        if (jwtUtils.validateToken(refreshToken, storedToken)) {
            blackListToken(accessToken, "refresh");
            MemberInfo memberInfo = memberAdapter.getMemberInfo(memberId);
            return issueJwt(memberInfo);
        } else {
            throw new InvalidRefreshToken();
        }
    }

    @Override
    public void blackListToken(String accessToken, String type) {
        redisDao.saveToken(accessToken, "", jwtUtils.extractExpirationTime(accessToken));
        redisDao.deleteToken(jwtUtils.extractMemberId(accessToken));
    }


}
