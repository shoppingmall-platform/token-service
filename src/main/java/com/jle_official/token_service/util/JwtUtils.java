package com.jle_official.token_service.util;

import com.jle_official.token_service.service.dto.MemberInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {
    private static final long EXPIRATION_MS_ACCESS= 1000 * 60 * 60;
    private static final long EXPIRATION_MS_REFRESH = 1000 * 60 * 60 * 7;

    @Value("${jwt.secret}")
    private static String secret;

    private static SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String generateAccessToken(MemberInfo memberInfo) {

        return Jwts.builder()
                .subject(String.valueOf(memberInfo.getMemberId()))
                .claim("id", memberInfo.getMemberId())
                .claim("role", memberInfo.getRole())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + EXPIRATION_MS_ACCESS))
                .signWith(getSigningKey())
                .compact();
    }

    public static String generateRefreshToken(MemberInfo memberInfo) {
        return Jwts.builder()
                .subject(String.valueOf(memberInfo.getMemberId()))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + EXPIRATION_MS_ACCESS))
                .signWith(getSigningKey())
                .compact();
    }

}
