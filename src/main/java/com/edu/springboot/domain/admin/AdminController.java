/**
 * 파일위치: src/main/java/com/edu/springboot/domain/admin/AdminController.java
 * 기능전체: 관리자 전용 기능 통합 컨트롤러 (회원, 승인, 사건, 커뮤니티, 신고, 통계 API)
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
import com.edu.springboot.domain.community.CommunityMapper;
import com.edu.springboot.domain.community.vo.PostVo;
import com.edu.springboot.domain.question.QuestionMapper;
import com.edu.springboot.domain.question.vo.QuestionVO;

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
	private final CommunityMapper communityMapper;
	private final QuestionMapper questionMapper;
	private final AdminMapper adminMapper;

	@PostConstruct
	public void init() {
		System.out.println("✅ [domain/admin] 관리자 컨트롤러가 가동되었습니다.");
	}

	@GetMapping("/stats.do")
	public ResponseEntity<?> getDashboardStats() {
		int totalPersonal = adminMapper.countTotalPersonalMembers();
		int totalLawyer = adminMapper.countTotalLawyerMembers();
		int totalCases = adminMapper.countCompletedCases();

		List<Map<String, Object>> dailyUsers = adminMapper.getDailyMemberStats();
		List<Map<String, Object>> dailyCases = adminMapper.getDailyCaseStats();

		return ResponseEntity.ok(Map.of("kpi",
				Map.of("totalPersonal", totalPersonal, "totalLawyer", totalLawyer, "totalCases", totalCases), "users",
				dailyUsers, "cases", dailyCases));
	}

	@GetMapping("/member/list.do")
	public ResponseEntity<?> getMemberList() {
		return ResponseEntity.ok(Map.of("data", memberMapper.selectAllMembers()));
	}

	@PostMapping("/member/delete.do")
	public ResponseEntity<?> suspendMember(@RequestBody Map<String, Long> payload) {
		try {
			Long memberId = payload.get("memberId");
			memberMapper.deleteMember(memberId);
			return ResponseEntity.ok(Map.of("success", true, "message", "회원 계정이 정지 처리되었습니다."));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "처리 중 오류가 발생했습니다."));
		}
	}

	@GetMapping("/lawyer/approve.do")
	public ResponseEntity<?> getPendingLawyers() {
		return ResponseEntity.ok(Map.of("data", lawyerMapper.findPendingLawyers()));
	}

	@PostMapping("/lawyer/approve.do")
	public ResponseEntity<?> approveLawyer(@RequestBody LawyerVO lawyerVO) {
		lawyerMapper.updateApproveStatus(lawyerVO);
		return ResponseEntity.ok(Map.of("message", "전문회원 승인 상태가 업데이트 되었습니다."));
	}

	@GetMapping("/case/list.do")
	public ResponseEntity<?> getAllCases() {
		return ResponseEntity.ok(Map.of("data", caseService.getAllCasesForAdmin()));
	}

	@GetMapping("/board/list.do")
	public ResponseEntity<?> getBoardList() {
		List<PostVo> posts = communityMapper.list();
		List<QuestionVO> questions = questionMapper.selectAllQuestions();

		return ResponseEntity.ok(Map.of("data", Map.of("posts", posts, "questions", questions)));
	}

	@PostMapping("/board/delete.do")
	public ResponseEntity<?> deleteBoardItem(@RequestBody Map<String, Object> payload) {
		try {
			String type = payload.get("type").toString();
			Long id = Long.valueOf(payload.get("id").toString());

			if ("POST".equals(type)) {
				communityMapper.updatePostStatus(id.intValue(), "DELETED");
			} else if ("QUESTION".equals(type)) {
				questionMapper.updateQuestionStatus(id, "CLOSED");
			}
			return ResponseEntity.ok(Map.of("success", true, "message", "삭제 완료"));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "삭제 중 오류가 발생했습니다."));
		}
	}

	@GetMapping("/report/list.do")
	public ResponseEntity<?> getReportList() {
		return ResponseEntity.ok(Map.of("data", reportService.getAllReports()));
	}

	@GetMapping("/report/detail.do")
	public ResponseEntity<?> getReportDetail(@RequestParam("reportId") Long reportId) {
		return ResponseEntity.ok(Map.of("data", reportService.getReportDetail(reportId)));
	}

	@PostMapping("/report/process.do")
	public ResponseEntity<?> processSanction(@RequestBody Map<String, Object> payload) {
		try {
			ReportVO rVO = new ReportVO();
			rVO.setReportId(Long.valueOf(payload.get("reportId").toString()));
			rVO.setStatus(payload.get("status").toString());
			rVO.setResultNote(payload.get("resultNote").toString());
			rVO.setHandledBy(1L);

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