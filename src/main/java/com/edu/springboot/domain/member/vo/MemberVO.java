/**
 * 파일위치: src/main/java/com/edu/springboot/domain/member/vo/MemberVO.java
 * 기능설명: TB_MEMBER 테이블의 데이터를 담는 Value Object 객체입니다.
 */
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
    // 회원 고유 번호 (PK, 자동증가)
    private Long memberId;
    
    // 로그인 아이디 (중복불가)
    private String loginId;
    
    // 비밀번호 해시값
    private String password;
    
    // 회원 유형 (PERSONAL / LAWYER / ADMIN)
    private String memberType;
    
    // 회원 이름
    private String name;
    
    // 휴대전화번호
    private String phone;
    
    // 이메일 주소
    private String email;
    
    // SMS 인증 여부 (Y / N)
    private String phoneVerified;
    
    // 계정 상태 (ACTIVE: 정상 / SUSPENDED: 정지 / WITHDRAWN: 탈퇴)
    private String status;
    
    // 아이디 저장 여부 (Y / N)
    private String saveIdYn;
    
    // 가입일시
    private Date createdAt;
    
    // 최종 수정일시
    private Date updatedAt;
}