package com.jle_official.token_service.common.security;

import com.jle_official.token_service.member.adaptor.MemberAdapter;
import com.jle_official.token_service.member.dto.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberAdapter memberAdapter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberInfo memberInfo = memberAdapter.getMemberInfo(username);
        String password = memberAdapter.getMemberCredential(username);

        return new PrincipalDetails(memberInfo, password);
    }
}
