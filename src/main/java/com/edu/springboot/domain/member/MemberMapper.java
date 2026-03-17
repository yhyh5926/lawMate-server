// src/main/java/com/edu/springboot/domain/member/MemberMapper.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

// DB의 회원 관련 테이블에 쿼리를 날리는 매퍼 인터페이스
@Mapper
public interface MemberMapper {

//	로그인할 때 아이디로 회원 정보 찾아오는 쿼리
	MemberVO findByLoginId(String loginId);

//	새로운 회원 데이터를 DB에 넣는 쿼리
	int insertMember(MemberVO member);

//	회원 정보 수정하는 쿼리
	int updateMember(MemberVO member);

//	회원 데이터를 아예 삭제해버리는 쿼리
	int deleteMember(Long memberId);

//	이름이랑 핸드폰 번호로 로그인 아이디 찾는 쿼리
	String findLoginIdByNameAndPhone(String name, String phone);

//	비밀번호 재설정하는 쿼리
	int updatePassword(String loginId, String newPassword);

//	회원 유형별로 목록 찾아오는 쿼리
	List<MemberVO> findMembersByType(String memberType);

//	전체 회원 목록 가져오는 쿼리
	List<MemberVO> selectAllMembers();

//	이메일 주소로 회원 정보 찾아오는 쿼리
	MemberVO findByEmail(String email);

//	고유 식별 번호로 계정 상태를 탈퇴로 바꾸는 쿼리
	int withdrawMember(Long memberId);

//	탈퇴한 지 30일 넘은 회원 목록 가져오는 쿼리
	List<MemberVO> findWithdrawnMembersForAnonymization();

//	회원 개인정보를 알아볼 수 없게 덮어쓰는 쿼리
	int anonymizeMember(MemberVO member);

//	로그인 아이디를 기준으로 계정 상태를 탈퇴로 바꾸는 쿼리
	int withdrawMemberByLoginId(String loginId);

//	로그인 아이디로 탈퇴 처리하는 쿼리
	int withdrawMember(String loginId);

//	핸드폰 인증 상태 업데이트하는 쿼리
	int updatePhoneVerified(@Param("isVerified") String isVerified, @Param("memberId") Long memberId);

//	파라미터를 맵으로 받아서 내가 쓴 글 목록 찾아오는 쿼리
	List<Map<String, Object>> findMyPosts(Map<String, Object> params);
}