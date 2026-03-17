// src/main/java/com/edu/springboot/domain/member/dto/FindDto.java
package com.edu.springboot.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 아이디나 비밀번호 찾을 때 요청 데이터를 담는 객체
@Getter
@Setter
@ToString
public class FindDto {
//	본인 확인을 위해 입력받는 이름
	private String name;

//	인증에 사용할 핸드폰 번호
	private String phone;

//	비밀번호 재설정할 때 누구 계정인지 찾기 위한 아이디
	private String loginId;

//	새로 바꿀 비밀번호
	private String newPassword;
}