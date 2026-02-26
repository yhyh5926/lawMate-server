/**
 * 파일위치: src/main/java/com/edu/springboot/domain/member/dto/JoinDto.java
 * 기능설명: 일반 및 전문회원 가입 요청 데이터를 담는 DTO 객체입니다.
 */
package com.edu.springboot.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinDto {
    // 사용자 로그인 아이디 (4자 이상)
    private String loginId;
    
    // 사용자 비밀번호 (BCrypt 암호화 예정)
    private String password;
    
    // 회원 유형 (PERSONAL: 일반 / LAWYER: 전문)
    private String memberType;
    
    // 회원 실제 이름
    private String name;
    
    // 휴대전화번호
    private String phone;
    
    // 선택 입력 이메일 주소
    private String email;
    
    // 아이디 저장 여부 (기본 N)
    private String saveIdYn;

    // --- 전문회원 전용 필드 (LawyerJoinFormPage 데이터) ---
    // 변호사 자격번호
    private String licenseNo;
    
    // 전문 분야 키워드
    private String specialty;
    
    // 법무법인 또는 사무소명
    private String officeName;
}