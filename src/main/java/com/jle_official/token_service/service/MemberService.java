package com.jle_official.token_service.service;

import com.jle_official.token_service.service.dto.MemberInfo;

public interface MemberService {
    boolean validateLogin(String loginId, String password);

    MemberInfo getMemberInfo(String loginId);
}
