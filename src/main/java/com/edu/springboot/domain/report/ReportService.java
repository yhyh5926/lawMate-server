/**
 * 파일위치: src/main/java/com/edu/springboot/domain/report/ReportService.java
 * 기능전체: 신고 목록 조회 및 제재 집행 비즈니스 로직을 처리합니다.
 */
package com.edu.springboot.domain.report;

import com.edu.springboot.domain.report.vo.ReportVO;
import com.edu.springboot.domain.report.vo.SanctionVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final ReportMapper reportMapper;

	@PostConstruct
	public void init() {
		System.out.println("✅ [domain/report] 신고 및 제재 관리 서비스가 정상적으로 로드되었습니다.");
	}

	// 전체 신고 목록 가져오기
	public List<ReportVO> getAllReports() {
		return reportMapper.findAllReports();
	}

	// 신고 상세 조회
	public ReportVO getReportDetail(Long reportId) {
		return reportMapper.findReportById(reportId);
	}

	// 사용자 신고 등록
	@Transactional
	public boolean registerReport(ReportVO reportVO) {
		return reportMapper.insertReport(reportVO) > 0;
	}

	/**
	 * 제재 처리 로직 (관리자용) 신고 상태를 업데이트하고 제재 내역을 생성합니다.
	 */
	@Transactional
	public boolean applySanction(ReportVO reportVO, SanctionVO sanctionVO) {
		// 1. 신고 상태 업데이트 (RESOLVED / DISMISSED)
		int reportResult = reportMapper.updateReportStatus(reportVO);

		// 2. 무혐의가 아니고 제재 정보가 존재하는 경우 제재 테이블에 내역 등록
		if (reportResult > 0 && sanctionVO != null && !"DISMISSED".equals(reportVO.getStatus())) {
			reportMapper.insertSanction(sanctionVO);
		}

		return reportResult > 0;
	}
}