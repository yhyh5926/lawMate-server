package com.edu.springboot.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
    // TB_MEMBER 기존 필드
    private Long memberId;
    private String loginId;
    private String password;
    private String memberType; 
    private String name;
    private String phone;
    private String email;
    private String provider;
    private String phoneVerified;
    private String status;
    private String saveIdYn;
    private Date createdAt;
    private Date updatedAt;

    // 💡 [추가] 변호사일 경우 채워질 필드 (TB_LAWYER)
    private Long lawyerId;      // 변호사 PK
    private String licenseNo;
    private String specialty;
    private String officeName;
    private String approveStatus; // PENDING, APPROVED, REJECTED
}