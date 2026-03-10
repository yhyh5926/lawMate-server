// src/main/java/com/edu/springboot/domain/member/MemberMapper.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {

	// 💡 로그인 시 사용되는 메서드 (XML의 id="findByLoginId"와 일치해야 함)
	MemberVO findByLoginId(String loginId);

	// 회원 가입
	int insertMember(MemberVO member);

	// 회원 정보 수정
	int updateMember(MemberVO member);

	// 회원 탈퇴 처리
	int deleteMember(Long memberId);

	// 아이디 찾기
	String findLoginIdByNameAndPhone(String name, String phone);

	// 비밀번호 재설정
	int updatePassword(String loginId, String newPassword);

	// 유형별 조회
	List<MemberVO> findMembersByType(String memberType);

	// 전체 회원 목록 조회 (관리자용)
	List<MemberVO> selectAllMembers();

	// 💡 [추가] 이메일로 회원 조회 (중복/탈퇴 검증용)
	MemberVO findByEmail(String email);

	// 💡 [추가] 회원 탈퇴 처리 (WITHDRAWN_AT 업데이트 포함)
	int withdrawMember(Long memberId);

	// 💡 [추가] 30일 경과 탈퇴 회원 조회 (스케줄러 비식별화용)
	List<MemberVO> findWithdrawnMembersForAnonymization();

	// 💡 [추가] 회원 정보 완전 비식별화 처리
	int anonymizeMember(MemberVO member);
	
	// 💡 XML과 맞춰서 String loginId를 받는 것으로 추가/수정
    int withdrawMember(String loginId);

    // 💡 [추가] 내가 쓴 글 목록 조회 쿼리 매핑
    List<Map<String, Object>> findMyPosts(@Param("memberId") Long memberId, @Param("type") String type);
}