// src/main/java/com/edu/springboot/domain/member/dto/JoinDto.java
package com.edu.springboot.domain.member.dto;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
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
    private String provider;

    // --- 일반회원 주소 필드 ---
    private String address;
    private String detailAddress;

    // --- 전문회원(변호사) 전용 필드 ---
    private String licenseNo;
    private String specialty;
    private String officeName;
    private String officeAddress;
    private String officeDetailAddr;

    // --- 다중 첨부파일 지원 ---
    private List<MultipartFile> files;
}