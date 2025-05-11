package com.jle_official.token_service.token.util;

import com.jle_official.token_service.member.dto.MemberInfo;
import com.jle_official.token_service.token.dto.StatusCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jle.jwt.access-token.expires}")
    private Duration accessTokenExpires;

    @Value("${jle.jwt.refresh-token.expires}")
    private Duration refreshTokenExpires;

    private final RSAUtil rsaUtil;

    public String generateAccessToken(MemberInfo memberInfo) {
        Map<String, String> header = new HashMap<>();
        header.put("alg", "RS256");
        header.put("typ", "JWT");

        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpires.toMillis());

        return Jwts.builder()
                .header().add(header).and()
                .subject(memberInfo.getMemberId())
                .claim("role", memberInfo.getAuthority())
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(rsaUtil.getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    public String generateRefreshToken(String memberId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpires.toMillis());

        return Jwts.builder()
                .subject(memberId)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(rsaUtil.getPrivateKey())
                .compact();
    }

    public StatusCode validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return StatusCode.INVALID;
        }

        try {
            getClaims(token);
            return StatusCode.VALID;

        } catch (ExpiredJwtException e) {
            return StatusCode.EXPIRED;

        } catch (Exception e) {
            return StatusCode.INVALID;
        }
    }

    public Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .verifyWith(rsaUtil.getPublicKey())
                .build()
                .parseSignedClaims(token);
    }


    public String extractMemberId(String token) {
        return getClaims(token).getPayload().getSubject();
    }

    public Duration extractExpirationTime(String token) {
        return Duration.ofMillis(getClaims(token).getPayload().getExpiration().getTime());
    }

}
