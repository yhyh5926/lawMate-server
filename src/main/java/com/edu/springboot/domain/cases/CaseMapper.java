package com.edu.springboot.domain.cases;

import com.edu.springboot.domain.cases.vo.CaseVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CaseMapper {
    List<CaseVO> findCasesByMemberId(Long memberId);
    CaseVO findCaseById(Long caseId);
    
    // 관리자용 전체 사건 조회
    List<CaseVO> findAllCases();
}