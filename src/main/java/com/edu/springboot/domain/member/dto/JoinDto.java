// src/main/java/com/edu/springboot/domain/member/dto/JoinDto.java
package com.edu.springboot.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinDto {
    private String loginId;
    private String password;
    private String memberType;
    private String name;
    private String phone;
    private String email;
    private String saveIdYn;

    // --- 전문회원(변호사) 전용 필드 ---
    private String licenseNo;
    private String specialty;
    private String officeName;
}