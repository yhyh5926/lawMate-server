/**
 * 파일위치: src/main/java/com/edu/springboot/domain/cases/CaseMapper.java
 * 기능전체: TB_CASE 테이블에 접근하는 MyBatis 매퍼입니다. 
 * 관리자용 전체 조회 기능(selectAllCases)이 추가되었습니다.
 */
package com.edu.springboot.domain.cases;

import com.edu.springboot.domain.cases.vo.CaseVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CaseMapper {
    
    // 특정 회원의 사건 목록 조회 (마이페이지용)
    List<CaseVO> selectCasesByMemberId(Long memberId);
    
    // 사건 상세 정보 조회
    CaseVO selectCaseById(Long caseId);
    
    // 사건 상태(STEP) 변경
    int updateCaseStep(Long caseId, String step);
    
    // [추가] 플랫폼 전체 사건 목록 조회 (관리자용)
    List<CaseVO> selectAllCases();
}