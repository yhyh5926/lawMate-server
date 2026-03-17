// src/main/java/com/edu/springboot/domain/member/vo/MemberVO.java
package com.edu.springboot.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

// 회원 정보를 데이터베이스랑 주고받을 때 쓰는 객체
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
//	회원 고유 식별 번호
	private Long memberId;

//	로그인 아이디
	private String loginId;

//	비밀번호
	private String password;

//	회원 유형
	private String memberType;

//	이름
	private String name;

//	핸드폰 번호
	private String phone;

//	이메일
	private String email;

//	가입 경로
	private String provider;

//	핸드폰 인증 여부
	private String phoneVerified;

//	현재 계정 상태
	private String status;

//	아이디 저장 여부
	private String saveIdYn;

//	가입 날짜
	private Date createdAt;

//	정보 수정 날짜
	private Date updatedAt;

//	기본 주소
	private String address;

//	상세 주소
	private String detailAddress;

//	변호사일 경우 연결되는 변호사 고유 식별 번호
	private Long lawyerId;

//	자격증 번호
	private String licenseNo;

//	전문 분야
	private String specialty;

//	사무소 이름
	private String officeName;

//	사무소 주소
	private String officeAddr;

//	사무소 상세 주소
	private String officeDetailAddr;

//	관리자 승인 상태
	private String approveStatus;

//	탈퇴한 날짜
	private Date withdrawnAt;
}