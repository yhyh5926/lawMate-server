/**
 * 파일 위치: src/main/java/com/edu/springboot/domain/member/MemberService.java
 * 수정 내용: 인터페이스에는 @Service 어노테이션을 사용하지 않습니다. 순수 인터페이스로 유지하여 빈 충돌을 방지합니다.
 */
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;
import java.util.List;
import java.util.Map;

public interface MemberService {
    // 아이디 사용 가능 여부 확인
    boolean isLoginIdAvailable(String loginId);

    // 회원가입 처리
    boolean join(JoinDto dto);

    // 로그인 처리 및 토큰 반환
    Map<String, Object> login(LoginDto dto);

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