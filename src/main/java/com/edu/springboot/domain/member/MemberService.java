// src/main/java/com/edu/springboot/domain/member/MemberService.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;
import java.util.List;
import java.util.Map;

// 회원 관련 비즈니스 로직 껍데기
public interface MemberService {
//	아이디 중복 검사 로직
	boolean isLoginIdAvailable(String loginId);

//	회원가입 처리 로직
	boolean join(JoinDto dto);

//	일반 로그인 및 토큰 발급 로직
	Map<String, Object> login(LoginDto dto);

//	구글 소셜 로그인 로직
	Map<String, Object> socialLogin(Map<String, String> socialData);

//	회원 상세 정보 가져오는 로직
	MemberVO getMemberInfo(String loginId);

//	프로필 수정 로직
	boolean updateProfile(MemberVO vo);

//	로그인 아이디로 탈퇴 처리하는 로직
	boolean withdraw(String loginId);

//	이름과 전화번호로 아이디 찾는 로직
	String findId(String name, String phone);

//	핸드폰 인증 번호 발송 로직
	String sendAuthCode(String phone);

//	탈퇴 후 30일 지났는지 확인하는 방어 로직
	void validateSignup(String loginId, String email);

//	고유 식별 번호로 회원 탈퇴 처리하는 로직
	boolean withdrawMember(Long memberId);

//	게시판 종류에 맞춰 내 글 찾아오는 로직
	List<Map<String, Object>> findMyPosts(Long memberId, String type);
}