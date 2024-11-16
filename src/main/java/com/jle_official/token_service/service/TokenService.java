package com.jle_official.token_service.service;

import com.jle_official.token_service.domain.Token;
import org.springframework.stereotype.Service;

public interface TokenService {
    Token issueToken(String loginId, String password);

    String reIssueToken(String refreshToken);
}
