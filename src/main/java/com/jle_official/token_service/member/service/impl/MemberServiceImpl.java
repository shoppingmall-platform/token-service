package com.jle_official.token_service.member.service.impl;

import com.jle_official.token_service.member.adaptor.MemberAdapter;
import com.jle_official.token_service.member.service.MemberService;
import com.jle_official.token_service.member.dto.MemberCredential;
import com.jle_official.token_service.member.dto.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberAdapter memberAdapter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean validateLogin(String loginId, String password) {
        MemberCredential memberCredential = memberAdapter.getMemberCredential(loginId);
        return passwordEncoder.matches(password, memberCredential.getPassword());
    }

    @Override
    public MemberInfo getMemberInfo(String loginId) {
        return memberAdapter.getMemberInfo(loginId);
    }
}
