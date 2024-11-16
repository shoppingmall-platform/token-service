package com.jle_official.token_service.member.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MemberInfo {
    private Long memberId;
    private String name;
    private String email;
    private LocalDate birthday;
    private String phoneNumber;
    private String gender;
    private String status;
    private String level;
    private String region;
    private LocalDateTime createAt;
    private Role role;
}
