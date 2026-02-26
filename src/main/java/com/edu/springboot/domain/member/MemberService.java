/**
 * 파일위치: src/main/java/com/edu/springboot/domain/member/MemberService.java
 * 기능전체: 회원 관련 비즈니스 로직의 명세를 정의하는 인터페이스입니다.
 * 💡 중요: 인터페이스에는 @Service 어노테이션을 절대로 붙이지 않습니다. (중복 빈 에러의 주원인)
 */
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;
import java.util.Map;
import java.util.List;

public interface MemberService {
    
    // 아이디 중복 체크
    boolean isLoginIdAvailable(String loginId);
    
    // 회원 가입 처리
    boolean join(JoinDto joinDto);
    
    // 로그인 처리 및 토큰 반환
    Map<String, Object> login(LoginDto loginDto);
    
    // 내 정보 조회
    MemberVO getMemberInfo(String loginId);
    
    // 프로필 수정
    boolean updateProfile(MemberVO memberVO);
    
    // 회원 탈퇴
    boolean withdraw(String loginId);
    
    // 아이디 찾기
    String findId(String name, String phone);
    
    // SMS 인증번호 발송 (가상)
    String sendAuthCode(String phone);

    // [추가] 회원 유형별 조회 (관리자용)
    List<MemberVO> getMembersByType(String memberType);
}