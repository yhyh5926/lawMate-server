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

    // 일반 로그인 처리 (1234 프리패스 및 승인 대기 차단 포함)
    Map<String, Object> login(LoginDto dto);
    
    // 💡 소셜 로그인 처리 (구글 전용)
    Map<String, Object> socialLogin(Map<String, String> socialData);

    // 회원 정보 상세 조회
    MemberVO getMemberInfo(String loginId);

    // 프로필 수정
    boolean updateProfile(MemberVO vo);

    // 회원 탈퇴 처리
    boolean withdraw(String loginId);

    // 성함과 연락처로 아이디 찾기
    String findId(String name, String phone);

    // 인증번호 발송 (테스트용)
    String sendAuthCode(String phone);

    // 관리자용: 회원 유형별 목록 조회
    List<MemberVO> getMembersByType(String memberType);
}