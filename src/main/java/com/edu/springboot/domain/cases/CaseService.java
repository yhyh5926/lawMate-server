package com.edu.springboot.domain.cases;

import com.edu.springboot.domain.cases.vo.CaseVO;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseService {
    
    private final CaseMapper caseMapper;

    public List<CaseVO> getMyCases(Long memberId) {
        return caseMapper.findCasesByMemberId(memberId);
    }

    public CaseVO getCaseDetail(Long caseId) {
        return caseMapper.findCaseById(caseId);
    }
    
    public List<CaseVO> getAllCases() {
        return caseMapper.findAllCases();
    }
}