// src/main/java/com/edu/springboot/domain/member/MemberService.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;
import java.util.List;
import java.util.Map;

public interface MemberService {
    // 아이디 사용 가능 여부 확인
    boolean isLoginIdAvailable(String loginId);

    // 회원가입 처리 (TB_MEMBER + TB_LAWYER 통합 저장)
    boolean join(JoinDto dto);

    // 💡 [수정] 일반 로그인 처리 (1234 임시 프리패스 제거 및 정상 암호화 검증 적용)
    Map<String, Object> login(LoginDto dto);
    
    // 소셜 로그인 처리 (구글 전용)
    Map<String, Object> socialLogin(Map<String, String> socialData);

    // 회원 정보 상세 조회
    MemberVO getMemberInfo(String loginId);

    // 프로필 수정
    boolean updateProfile(MemberVO vo);

    // 회원 탈퇴 처리 (기존 loginId 기준)
    boolean withdraw(String loginId);

    // 성함과 연락처로 아이디 찾기
    String findId(String name, String phone);

    // 인증번호 발송 (테스트용)
    String sendAuthCode(String phone);

    // 💡 [추가] 회원가입 30일 방어막 검증
    void validateSignup(String loginId, String email);

    // 💡 [추가] 프론트엔드 연동을 위한 회원 탈퇴 처리 (memberId 기준)
    boolean withdrawMember(Long memberId);

    // 💡 [추가] 내가 쓴 글 목록 조회 (DB 연동용)
    List<Map<String, Object>> getMyPosts(Long memberId, String type);
}