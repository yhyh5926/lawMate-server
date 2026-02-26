/**
 * 파일위치: src/main/java/com/edu/springboot/domain/member/dto/LoginDto.java
 * 기능설명: 로그인 요청 시 아이디와 비밀번호 데이터를 전달받는 DTO 객체입니다.
 */
package com.edu.springboot.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginDto {
    // 사용자가 입력한 로그인 아이디
    private String loginId;
    
    // 사용자가 입력한 비밀번호
    private String password;
}