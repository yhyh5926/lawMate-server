// IntelliJ
// 파일위치: src/main/java/com/edu/springboot/domain/cases/CaseMapper.java

package com.edu.springboot.domain.cases;

import com.edu.springboot.domain.cases.vo.CaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CaseMapper {

	// 특정 회원의 사건 목록 조회 (마이페이지용)
	List<CaseVO> selectCasesByMemberId(Long memberId);

	// 사건 상세 정보 조회
	CaseVO selectCaseById(Long caseId);

	// 사건 상태(STEP) 변경
	int updateCaseStep(@Param("caseId") Long caseId, @Param("step") String step);

	// 플랫폼 전체 사건 목록 조회 (관리자용)
	List<CaseVO> selectAllCases();

	// 내용 수정 및 코멘트 추가 메서드 선언
	int updateCaseInfo(CaseVO caseVO);

	// 새로운 사건 직접 등록 메서드 선언
	int insertManualCase(CaseVO caseVO);

	// 기존 자동 등록 메서드 유지
	int insertCase(CaseVO caseVO);

	// 사건 등록 시, 상담 DB(TB_CONSULT)에 연결된 CASE_ID 업데이트용
	int updateConsultCaseId(@Param("consultId") Long consultId, @Param("caseId") Long caseId);

	// 사건 관리 탭 리뷰 작성 연동
	Long selectConsultIdByCaseId(@Param("caseId") Long caseId);

	// 과거 데이터를 위한 강제 매핑용
	Long selectFallbackConsultId(@Param("memberId") Long memberId, @Param("lawyerId") Long lawyerId);

	int insertCaseReview(@Param("consultId") Long consultId, @Param("memberId") Long memberId,
			@Param("lawyerId") Long lawyerId, @Param("rating") int rating, @Param("content") String content);

	// 💡 [에러 원인 제거] updateLawyerReviewStats 선언 삭제 완료
}