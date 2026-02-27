/**
 * 파일위치: src/main/java/com/edu/springboot/domain/cases/CaseService.java
 * 기능전체: 사건 관련 비즈니스 로직을 처리합니다.
 * AdminController에서 호출하는 getAllCasesForAdmin 메서드가 추가되었습니다.
 */
package com.edu.springboot.domain.cases;

import com.edu.springboot.domain.cases.vo.CaseVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseService {

    private final CaseMapper caseMapper;

    @PostConstruct
    public void init() {
        // 폴더별 작동 확인 로그 (은혁 파트)
        System.out.println("✅ [domain/cases] 사건 관리 서비스가 정상 작동 중입니다.");
    }

    public List<CaseVO> getCaseListByMember(Long memberId) {
        return caseMapper.selectCasesByMemberId(memberId);
    }

    public CaseVO getCaseDetail(Long caseId) {
        return caseMapper.selectCaseById(caseId);
    }

    // [추가] 관리자용 전체 사건 조회 메서드 (AdminController 오류 해결)
    public List<CaseVO> getAllCasesForAdmin() {
        return caseMapper.selectAllCases();
    }
}