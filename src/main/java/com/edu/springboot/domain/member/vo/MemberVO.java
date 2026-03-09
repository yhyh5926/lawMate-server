// src/main/java/com/edu/springboot/domain/member/vo/MemberVO.java
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

	// 💡 [오류 수정] DB에 추가된 주소 필드 선언
	private String address;
	private String detailAddress;

	// 💡 [추가] 변호사일 경우 채워질 필드 (TB_LAWYER)
	private Long lawyerId; // 변호사 PK
	private String licenseNo;
	private String specialty;
	private String officeName;
	private String approveStatus; // PENDING, APPROVED, REJECTED

	// 💡 [추가] 탈퇴 일시 (30일 재가입 방지용)
	private Date withdrawnAt;
}