package com.jle_official.token_service.common.security;

import com.jle_official.token_service.member.adaptor.MemberAdapter;
import com.jle_official.token_service.member.dto.MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberAdapter memberAdapter;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        MemberInfo memberInfo = memberAdapter.getMemberInfo(memberId);
        String password = memberAdapter.getMemberCredential(memberId);
        log.debug("member Id: {}", memberInfo.getMemberId());
        log.debug("password: {}", password);

        return new PrincipalDetails(memberInfo, password);
    }
}
