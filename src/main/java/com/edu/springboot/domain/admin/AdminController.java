/**
 * 파일위치: src/main/java/com/edu/springboot/domain/admin/AdminController.java
 * 기능전체: 관리자 전용 회원관리, 승인, 사건 모니터링, 신고 및 제재 API를 제공합니다.
 */
package com.edu.springboot.domain.admin;

import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edu.springboot.domain.cases.CaseService;
import com.edu.springboot.domain.lawyer.LawyerMapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;
import com.edu.springboot.domain.member.MemberMapper;
import com.edu.springboot.domain.report.ReportService;
import com.edu.springboot.domain.report.vo.ReportVO;
import com.edu.springboot.domain.report.vo.SanctionVO;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final LawyerMapper lawyerMapper;
	private final CaseService caseService;
	private final MemberMapper memberMapper;
	private final ReportService reportService;

	@PostConstruct
	public void init() {
		System.out.println("✅ [domain/admin] 관리자 컨트롤러가 가동되었습니다.");
	}

	// [기능유지] 전체 회원 목록 조회
	@GetMapping("/member/list.do")
	public ResponseEntity<?> getMemberList() {
		return ResponseEntity.ok(Map.of("data", memberMapper.selectAllMembers()));
	}

	// [기능유지] 전문회원 승인 대기 목록
	@GetMapping("/lawyer/approve.do")
	public ResponseEntity<?> getPendingLawyers() {
		return ResponseEntity.ok(Map.of("data", lawyerMapper.findPendingLawyers()));
	}

	// [기능유지] 전문회원 승인/반려 처리
	@PostMapping("/lawyer/approve.do")
	public ResponseEntity<?> approveLawyer(@RequestBody LawyerVO lawyerVO) {
		lawyerMapper.updateApproveStatus(lawyerVO);
		return ResponseEntity.ok(Map.of("message", "전문회원 승인 상태가 업데이트 되었습니다."));
	}

	// [기능유지] 플랫폼 전체 사건 모니터링
	@GetMapping("/case/list.do")
	public ResponseEntity<?> getAllCases() {
		return ResponseEntity.ok(Map.of("data", caseService.getAllCasesForAdmin()));
	}

	// [신규연동] 신고 접수 목록 조회 (실제 DB 데이터)
	@GetMapping("/report/list.do")
	public ResponseEntity<?> getReportList() {
		return ResponseEntity.ok(Map.of("data", reportService.getAllReports()));
	}

	// [신규연동] 신고 상세 조회
	@GetMapping("/report/detail.do")
	public ResponseEntity<?> getReportDetail(@RequestParam("reportId") Long reportId) {
		return ResponseEntity.ok(Map.of("data", reportService.getReportDetail(reportId)));
	}

	// [신규연동] 제재 처리 및 결과 저장
	@PostMapping("/report/process.do")
	public ResponseEntity<?> processSanction(@RequestBody Map<String, Object> payload) {
		try {
			ReportVO rVO = new ReportVO();
			rVO.setReportId(Long.valueOf(payload.get("reportId").toString()));
			rVO.setStatus(payload.get("status").toString());
			rVO.setResultNote(payload.get("resultNote").toString());
			rVO.setHandledBy(1L); // TODO: 세션 관리자 ID 적용

			SanctionVO sVO = null;
			if (payload.get("sanctionType") != null && !"NONE".equals(payload.get("sanctionType"))) {
				sVO = new SanctionVO();
				sVO.setMemberId(Long.valueOf(payload.get("targetMemberId").toString()));
				sVO.setReportId(rVO.getReportId());
				sVO.setSanctionType(payload.get("sanctionType").toString());
				sVO.setReason(rVO.getResultNote());
				sVO.setAdminId(rVO.getHandledBy());
			}

			boolean success = reportService.applySanction(rVO, sVO);
			return ResponseEntity.ok(Map.of("success", success));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("success", false, "error", e.getMessage()));
		}
	}
}