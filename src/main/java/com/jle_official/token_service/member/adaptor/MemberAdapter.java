package com.jle_official.token_service.member.adaptor;

import com.jle_official.token_service.common.exception.MemberAPIException;
import com.jle_official.token_service.member.dto.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MemberAdapter {
    private final RestTemplate restTemplate;

    @Value("${jle.api.member}")
    private String MEMBER_API_URL;

    public String getMemberCredential(String loginId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                MEMBER_API_URL+"/credential/"+loginId,
                HttpMethod.GET,
                request,
                Map.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new MemberAPIException("getMemberCredential() error", response);
        }

        return response.getBody().get("password").toString();
    }

    public MemberInfo getMemberInfo(String loginId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<MemberInfo> response = restTemplate.exchange(
                MEMBER_API_URL+"/"+loginId,
                HttpMethod.GET,
                request,
                MemberInfo.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new MemberAPIException("getMemberInfo() error", response);
        }
        return response.getBody();
    }
}
