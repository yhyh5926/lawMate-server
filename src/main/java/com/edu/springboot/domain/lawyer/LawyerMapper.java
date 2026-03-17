// src/main/java/com/edu/springboot/domain/lawyer/LawyerMapper.java
package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

// 데이터베이스의 변호사 관련 테이블과 쿼리를 매핑하는 인터페이스
@Mapper
public interface LawyerMapper {

//	데이터베이스에 등록된 모든 변호사 정보를 조회하는 쿼리
	List<LawyerVO> selectAllLawyers();
//	변호사 고유 아이디로 특정 변호사 정보를 상세 조회하는 쿼리
	LawyerVO selectLawyerById(Long lawyerId);
//	관리자 승인을 대기 중인 변호사 목록을 가져오는 쿼리
	List<LawyerVO> findPendingLawyers();
//	가입 신청한 변호사의 승인 상태를 업데이트하는 쿼리
	int updateApproveStatus(LawyerVO lawyerVO);
//	새로운 변호사 정보를 데이터베이스에 등록하는 쿼리
	int insertLawyer(LawyerVO lawyerVO);
//	회원 아이디를 이용해 연결된 변호사 정보를 찾아오는 쿼리
	LawyerVO selectLawyerByMemberId(Long memberId);
//	변호사의 프로필 상세 정보를 수정해서 데이터베이스에 반영하는 쿼리
	int updateLawyerProfile(LawyerVO lawyerVO);
}