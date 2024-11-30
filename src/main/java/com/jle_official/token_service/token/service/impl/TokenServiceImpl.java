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
    public Token issueJwt(PrincipalDetails principalDetails) {

//        MemberInfo memberInfo = memberAdapter.getMemberInfo(principalDetails.getUsername());
//        return issueToken(String.valueOf(memberInfo.getMemberId()));
        return issueToken(principalDetails.getUsername());
    }

    @Override
    public Token reissueToken(String accessToken, String refreshToken) {
        String memberId = jwtUtils.extractMemberId(accessToken);
        String storedToken = redisDao.getToken(memberId);

        if (jwtUtils.validateToken(refreshToken, storedToken)) {
            blackListToken(accessToken, "refresh");
            return issueToken(memberId);
        } else {
            throw new InvalidRefreshToken();
        }
    }

    @Override
    public void blackListToken(String accessToken, String type) {
        redisDao.saveToken(accessToken, "", jwtUtils.extractExpirationTime(accessToken));
        redisDao.deleteToken(jwtUtils.extractMemberId(accessToken));
    }

    private Token issueToken(String id) {
        String accessToken = jwtUtils.generateAccessToken(id);
        String refreshToken = jwtUtils.generateRefreshToken(id);
        Duration expiration = jwtUtils.extractExpirationTime(refreshToken);

        redisDao.saveToken(id, refreshToken, expiration);
        return new Token(accessToken, refreshToken);
    }


}
