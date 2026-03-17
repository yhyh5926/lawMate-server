// src/main/java/com/edu/springboot/domain/member/dto/LoginDto.java
package com.edu.springboot.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 로그인할 때 입력한 아이디랑 비밀번호를 받는 객체
@Getter
@Setter
@ToString
public class LoginDto {
//	로그인 아이디
	private String loginId;

//	로그인 비밀번호
	private String password;
}