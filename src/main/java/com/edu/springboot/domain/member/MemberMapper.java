// src/main/java/com/edu/springboot/domain/member/MemberMapper.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {

	// 💡 로그인 시 사용되는 메서드
	MemberVO findByLoginId(String loginId);

	// 회원 가입
	int insertMember(MemberVO member);

	// 회원 정보 수정
	int updateMember(MemberVO member);

	// 회원 탈퇴 처리 (ID 기준 완전 삭제)
	int deleteMember(Long memberId);

	// 아이디 찾기
	String findLoginIdByNameAndPhone(String name, String phone);

	// 비밀번호 재설정
	int updatePassword(String loginId, String newPassword);

	// 유형별 조회
	List<MemberVO> findMembersByType(String memberType);

	// 전체 회원 목록 조회 (관리자용)
	List<MemberVO> selectAllMembers();

	// 이메일로 회원 조회
	MemberVO findByEmail(String email);

	// 💡 회원 탈퇴 처리 (memberId 기준 STATUS 업데이트)
	int withdrawMember(Long memberId);

	// 30일 경과 탈퇴 회원 조회
	List<MemberVO> findWithdrawnMembersForAnonymization();

	// 회원 정보 완전 비식별화 처리
	int anonymizeMember(MemberVO member);

	// 💡 [복구] loginId 기반 탈퇴 메서드 (XML의 withdrawMemberByLoginId와 매핑)
	int withdrawMemberByLoginId(String loginId);

	// 💡 [복구] XML의 withdrawMember(String)와 매핑되는 메서드
	int withdrawMember(String loginId);

	// 휴대폰 인증 상태 업데이트
	int updatePhoneVerified(@Param("isVerified") String isVerified, @Param("memberId") Long memberId);

	// 💡 [해결] Map을 파라미터로 받아 500 에러 해결
	List<Map<String, Object>> findMyPosts(Map<String, Object> params);
}