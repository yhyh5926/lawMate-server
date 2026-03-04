// src/main/java/com/edu/springboot/domain/member/MemberMapper.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

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

}