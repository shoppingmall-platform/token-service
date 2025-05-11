package com.jle_official.token_service.member.dto;

import com.jle_official.token_service.token.util.RSAUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MemberInfo {
    private String memberId;
    private String name;
    private String email;
    private LocalDate birthday;
    private String phoneNumber;
    private String gender;
    private String status;
    private String authority;
    private String level;
    private String region;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
}
