package com.edu.springboot.domain.member.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MemberVO {
    private Long memberId;
    private String loginId;
    private String password;
    private String memberType; // PERSONAL / LAWYER
    private String name;
    private String phone;
    private String email;
    private String phoneVerified;
    private String status; // ACTIVE / SUSPENDED / WITHDRAWN
    private String saveIdYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}