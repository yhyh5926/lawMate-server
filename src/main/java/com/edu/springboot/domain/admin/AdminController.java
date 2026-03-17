// src/main/java/com/edu/springboot/domain/admin/AdminController.java
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
import com.edu.springboot.domain.poll.vo.PollVo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

// 관리자 기능을 처리하기 위한 전용 REST 컨트롤러
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

//	서버가 실행될 때 관리자 컨트롤러가 정상적으로 매핑되었는지 확인하기 위한 초기화 메서드
	@PostConstruct
	public void init() {
		System.out.println("[domain/admin] 관리자 컨트롤러가 가동되었습니다.");
	}

//	관리자 대시보드 메인 화면에 보여줄 전체 KPI 수치와 날짜별 차트 데이터를 한 번에 묶어서 반환하는 메서드
	@GetMapping("/stats")
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

//	서비스에 가입된 전체 회원 목록을 불러오는 메서드
	@GetMapping("/member/list")
	public ResponseEntity<?> getMemberList() {
		return ResponseEntity.ok(Map.of("data", memberMapper.selectAllMembers()));
	}

//	문제가 있는 회원의 계정을 관리자 권한으로 강제 정지 처리하는 메서드
	@PostMapping("/member/delete")
	public ResponseEntity<?> suspendMember(@RequestBody Map<String, Long> payload) {
		try {
			Long memberId = payload.get("memberId");
			memberMapper.deleteMember(memberId);
			return ResponseEntity.ok(Map.of("success", true, "message", "회원 계정이 정지 처리되었습니다."));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "처리 중 오류가 발생했습니다."));
		}
	}

//	변호사로 가입 후 승인을 기다리고 있는 대기 상태의 회원 목록을 불러오는 메서드
	@GetMapping("/lawyer/approve")
	public ResponseEntity<?> getPendingLawyers() {
		return ResponseEntity.ok(Map.of("data", lawyerMapper.findPendingLawyers()));
	}

//	관리자가 대기 중인 변호사 회원을 확인하고 최종 가입 승인을 해주는 메서드
	@PostMapping("/lawyer/approve")
	public ResponseEntity<?> approveLawyer(@RequestBody LawyerVO lawyerVO) {
		lawyerMapper.updateApproveStatus(lawyerVO);
		return ResponseEntity.ok(Map.of("message", "전문회원 승인 상태가 업데이트 되었습니다."));
	}

//	접수된 전체 사건 리스트를 관리자용으로 전부 불러오는 메서드
	@GetMapping("/case/list")
	public ResponseEntity<?> getAllCases() {
		return ResponseEntity.ok(Map.of("data", caseService.getAllCasesForAdmin()));
	}

//	게시물 관리를 위해 자유, 질문, 의견조사 게시판의 데이터를 불러오는 메서드
//	관리자 페이지 특성상 페이징 처리 없이 각 게시판별로 넉넉하게 500개씩 한 번에 가져와서 프론트로 전달한다
	@GetMapping("/board/list")
	public ResponseEntity<?> getBoardList() {
		List<PostVo> posts = communityMapper.list("", 1, 500);
		List<QuestionVO> questions = questionMapper.selectAllQuestions();
		List<PollVo> polls = communityMapper.pollList("latest", 1, 500);

		return ResponseEntity.ok(Map.of("data", Map.of("posts", posts, "questions", questions, "polls", polls)));
	}

//	관리자가 부적절한 게시물을 확인하고 삭제 상태로 변경하는 메서드
//	프론트에서 넘어온 게시글 종류에 맞춰 각각 다른 매퍼를 호출해서 삭제 처리한다
	@PostMapping("/board/delete")
	public ResponseEntity<?> deleteBoardItem(@RequestBody Map<String, Object> payload) {
		try {
			String type = payload.get("type").toString();
			Long id = Long.valueOf(payload.get("id").toString());

			if ("POST".equals(type)) {
				communityMapper.updatePostStatus(id.intValue(), "DELETED");
			} else if ("QUESTION".equals(type)) {
				questionMapper.updateQuestionStatus(id, "CLOSED", null);
			} else if ("POLL".equals(type)) {
				communityMapper.updatePollStatus(id.intValue(), "DELETED");
			}
			return ResponseEntity.ok(Map.of("success", true, "message", "삭제 완료"));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "삭제 중 오류가 발생했습니다."));
		}
	}

//	유저들이 접수한 전체 신고 내역을 불러오는 메서드
	@GetMapping("/report/list")
	public ResponseEntity<?> getReportList() {
		return ResponseEntity.ok(Map.of("data", adminMapper.selectAllReports()));
	}

//	관리자가 확인하려는 특정 신고 건의 상세 내용을 조회하는 메서드
	@GetMapping("/report/detail")
	public ResponseEntity<?> getReportDetail(@RequestParam("reportId") Long reportId) {
		return ResponseEntity.ok(Map.of("data", adminMapper.selectReportDetail(reportId)));
	}

//	관리자가 신고 내용을 확인한 뒤 제재를 가하거나 처리 완료하는 메서드
//	처리 결과와 함께 제재 내용이 있다면 SanctionVO에 담아서 같이 데이터베이스에 적용한다
	@PostMapping("/report/process")
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

//	전체 결제 내역을 불러오는 메서드
	@GetMapping("/payment/list")
	public ResponseEntity<?> getPaymentList() {
		return ResponseEntity.ok(Map.of("data", adminMapper.selectAllPayments()));
	}

//	변호사들에게 수익을 정산해준 전체 내역을 불러오는 메서드
	@GetMapping("/settlement/list")
	public ResponseEntity<?> getSettlementList() {
		return ResponseEntity.ok(Map.of("data", adminMapper.selectAllSettlements()));
	}
}