package com.jle_official.token_service.member.service;

import com.jle_official.token_service.member.dto.MemberInfo;

public interface MemberService {
    boolean validateLogin(String loginId, String password);

    MemberInfo getMemberInfo(String loginId);
}
