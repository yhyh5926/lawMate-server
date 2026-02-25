package com.edu.springboot.domain.member.dto;

import lombok.Data;

@Data
public class JoinDto {
    // 공통 회원 정보
    private String loginId;
    private String password;
    private String passwordConfirm;
    private String name;
    private String phone;
    private String email;
    
    // 전문회원(변호사) 전용 추가 정보
    private String licenseNo;
    private String specialty;
    private String officeName;
}