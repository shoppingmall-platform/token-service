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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberInfo memberInfo = memberAdapter.getMemberInfo(username);
        String password = memberAdapter.getMemberCredential(username);
        log.debug("member Id: {}", memberInfo.getId());
        log.debug("password: {}", password);

        return new PrincipalDetails(memberInfo, password);
    }
}
