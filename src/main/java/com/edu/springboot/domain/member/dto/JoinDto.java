// src/main/java/com/edu/springboot/domain/member/dto/JoinDto.java
package com.edu.springboot.domain.member.dto;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 회원가입할 때 프론트에서 넘어오는 데이터들을 다 받아주는 객체
@Getter
@Setter
@ToString
public class JoinDto {
//	로그인 아이디
	private String loginId;
//	비밀번호
	private String password;
//	일반 유저인지 변호사인지 구분하는 값
	private String memberType;
//	회원 이름
	private String name;
//	핸드폰 번호
	private String phone;
//	이메일 주소
	private String email;
//	아이디 저장 여부
	private String saveIdYn;
//	카카오, 구글 등 소셜 로그인 출처
	private String provider;

//	일반 회원 기본 주소
	private String address;
//	일반 회원 상세 주소
	private String detailAddress;

//	변호사 자격증 번호
	private String licenseNo;

//	변호사 전문 분야
	private String specialty;

//	법률 사무소 이름
	private String officeName;

//	사무소 기본 주소
	private String officeAddress;

//	사무소 상세 주소
	private String officeDetailAddr;

//	가입할 때 올린 증빙 서류나 프로필 사진 파일들
	private List<MultipartFile> files;
}