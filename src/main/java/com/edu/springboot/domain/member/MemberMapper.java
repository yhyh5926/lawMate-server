/**
 * 파일위치: src/main/java/com/edu/springboot/domain/member/MemberMapper.java
 * 기능전체: TB_MEMBER 테이블과 매핑되어 DB에 접근하는 인터페이스입니다. 
 * 수정사항: 일반/변호사/관리자 통합 계정 관리를 수행하며, 유형별 조회를 위한 메서드를 추가했습니다.
 */
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MemberMapper {
    
    // 아이디로 회원 정보 조회 (null 반환 가능)
    MemberVO findByLoginId(String loginId);
    
    // 회원 가입 (일반/변호사 계정 공통 생성)
    int insertMember(MemberVO member);
    
    // 회원 정보 수정
    int updateMember(MemberVO member);
    
    // 회원 탈퇴 처리 (상태 변경)
    int deleteMember(Long memberId);
    
    // 아이디 찾기 (이름 + 전화번호로 로그인 아이디 문자열 반환)
    String findLoginIdByNameAndPhone(String name, String phone);
    
    // 비밀번호 재설정
    int updatePassword(String loginId, String newPassword);

    // [추가] 회원 유형별 목록 조회 (관리자 페이지에서 변호사 회원만 혹은 일반 회원만 조회할 때 사용)
    List<MemberVO> findMembersByType(String memberType);
}