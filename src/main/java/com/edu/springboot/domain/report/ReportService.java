/**
 * 파일위치: src/main/java/com/edu/springboot/domain/report/ReportService.java
 * 기능전체: 신고 접수 및 관리자의 제재 집행 비즈니스 로직을 처리합니다.
 */
package com.edu.springboot.domain.report;

import com.edu.springboot.domain.report.vo.ReportVO;
import com.edu.springboot.domain.report.vo.SanctionVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportMapper reportMapper;

    @PostConstruct
    public void init() {
        // 서버 실행 시 폴더별 작동 확인 로그 (박은혁 담당)
        System.out.println("✅ [domain/report] 신고 및 제재 관리 서비스가 정상적으로 로드되었습니다.");
    }

    // 신고 등록 로직
    @Transactional
    public boolean registerReport(ReportVO reportVO) {
        return reportMapper.insertReport(reportVO) > 0;
    }

    /**
     * 제재 처리 로직 (관리자용)
     * 신고 상태를 RESOLVED로 바꾸고 제재 내역을 생성합니다.
     */
    @Transactional
    public boolean applySanction(ReportVO reportVO, SanctionVO sanctionVO) {
        // 1. 신고 상태 업데이트
        reportMapper.updateReportStatus(reportVO);
        // 2. 제재 이력 등록
        return reportMapper.insertSanction(sanctionVO) > 0;
    }
}