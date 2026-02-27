/**
 * 파일위치: src/main/java/com/edu/springboot/domain/member/dto/FindDto.java
 * 기능설명: 아이디 및 비밀번호 찾기 요청 데이터를 전달하는 DTO 객체입니다.
 */
package com.edu.springboot.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FindDto {
    // 아이디 찾기 및 비밀번호 재설정 확인 시 사용되는 회원 이름
    private String name;
    
    // SMS 인증 및 정보 확인에 사용되는 휴대전화번호
    private String phone;
    
    // 비밀번호 재설정 시 식별을 위한 로그인 아이디
    private String loginId;
    
    // 재설정할 새로운 비밀번호
    private String newPassword;
}