// src/main/java/com/edu/springboot/domain/cases/CaseMapper.java
package com.edu.springboot.domain.cases;

import com.edu.springboot.domain.cases.vo.CaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

// 데이터베이스의 사건 테이블과 쿼리를 연결해주는 매퍼
@Mapper
public interface CaseMapper {

//	마이페이지에서 특정 회원이 관련된 사건 목록을 모두 가져오는 쿼리
	List<CaseVO> selectCasesByMemberId(Long memberId);

//	사건 고유 아이디를 통해 해당 사건의 상세 정보를 가져오는 쿼리
	CaseVO selectCaseById(Long caseId);

//	해당 사건의 현재 진행 상태를 변경하는 쿼리
	int updateCaseStep(@Param("caseId") Long caseId, @Param("step") String step);

//	관리자 페이지에서 플랫폼 내 모든 사건 목록을 조회하는 쿼리
	List<CaseVO> selectAllCases();

//	사건의 내용이나 변호사의 전문 의견을 수정하는 쿼리
	int updateCaseInfo(CaseVO caseVO);

//	시스템 외부에서 접수된 사건을 변호사가 직접 등록하는 쿼리
	int insertManualCase(CaseVO caseVO);

//	시스템 내에서 자동으로 사건을 등록할 때 사용하는 쿼리
	int insertCase(CaseVO caseVO);

//	사건 등록 후 상담 테이블에 새로 생성된 사건의 ID를 연결해주는 쿼리
	int updateConsultCaseId(@Param("consultId") Long consultId, @Param("caseId") Long caseId);

//	특정 사건의 ID를 이용해 원본 상담 글의 ID를 찾아내는 쿼리
	Long selectConsultIdByCaseId(@Param("caseId") Long caseId);

//	원본 상담 글의 ID를 찾지 못했을 경우 회원과 변호사 정보를 조합해 과거 상담 기록을 유추하는 쿼리
	Long selectFallbackConsultId(@Param("memberId") Long memberId, @Param("lawyerId") Long lawyerId);

//	사건이 종료된 후 의뢰인이 남긴 평점과 후기 내용을 상담 리뷰 테이블에 저장하는 쿼리
	int insertCaseReview(@Param("consultId") Long consultId, @Param("memberId") Long memberId,
			@Param("lawyerId") Long lawyerId, @Param("rating") int rating, @Param("content") String content);

}