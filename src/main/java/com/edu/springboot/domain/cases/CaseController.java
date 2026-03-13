// IntelliJ
// 파일위치: src/main/java/com/edu/springboot/domain/cases/CaseController.java

package com.edu.springboot.domain.cases;

import com.edu.springboot.domain.cases.vo.CaseVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseController {

	private final CaseService caseService;

	@PostConstruct
	public void init() {
		System.out.println("✅ [domain/cases] 사건 관리 컨트롤러가 가동되었습니다.");
	}

	// 1. 내 사건 목록 조회
	@GetMapping("/list")
	public ResponseEntity<?> getMyCaseList(@RequestParam("memberId") Long memberId) {
		return ResponseEntity.ok(caseService.getCaseListByMember(memberId));
	}

	// 2. 사건 상세 조회
	@GetMapping("/detail")
	public ResponseEntity<?> getCaseDetail(@RequestParam("caseId") Long caseId) {
		return ResponseEntity.ok(caseService.getCaseDetail(caseId));
	}

	// 3. 사건 진행 상태(STEP) 업데이트
	@PutMapping("/{caseId}/status")
	public ResponseEntity<?> updateCaseStatus(@PathVariable("caseId") Long caseId,
			@RequestBody Map<String, String> payload) {
		String statusKey = payload.get("status");
		if (statusKey != null) {
			caseService.updateCaseStep(caseId, statusKey);
		}
		return ResponseEntity.ok(Map.of("success", true));
	}

	// 4. 파일 및 내용 수정
	@PutMapping("/{caseId}")
	public ResponseEntity<?> updateCaseInfo(@PathVariable("caseId") Long caseId,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "expertOpinion", required = false) String expertOpinion,
			@RequestParam(value = "files", required = false) List<MultipartFile> files) {

		CaseVO caseVO = new CaseVO();
		caseVO.setCaseId(caseId);
		caseVO.setDescription(description != null ? description : "");
		caseVO.setExpertOpinion(expertOpinion != null ? expertOpinion : "");

		caseService.updateCaseInfoWithFiles(caseVO, files);
		return ResponseEntity.ok(Map.of("success", true));
	}

	// 💡 5. [수정] 의뢰인 사건 완료 후기 등록 - DB 연동 완료
	@PostMapping("/{caseId}/review")
	public ResponseEntity<?> submitReview(@PathVariable("caseId") Long caseId,
			@RequestBody Map<String, Object> payload) {
		try {
			int rating = Integer.parseInt(payload.get("rating").toString());
			String content = payload.get("content").toString();
			caseService.submitCaseReview(caseId, rating, content);
			return ResponseEntity.ok(Map.of("success", true));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "리뷰 등록 실패"));
		}
	}

	// 6. 변호사가 마이페이지에서 수동으로 사건 등록 (Consult 연동 포함)
	@PostMapping("/manual-register")
	public ResponseEntity<?> createCaseManual(@RequestBody Map<String, Object> payload) {
		try {
			CaseVO caseVO = new CaseVO();
			caseVO.setLawyerId(Long.valueOf(payload.get("lawyerId").toString()));
			caseVO.setMemberId(Long.valueOf(payload.get("memberId").toString()));
			caseVO.setTitle(payload.get("title").toString());
			caseVO.setCaseType(payload.get("caseType").toString());
			caseVO.setDescription(payload.get("description").toString());

			Long consultId = null;
			if (payload.get("consultId") != null && !payload.get("consultId").toString().isEmpty()) {
				consultId = Long.valueOf(payload.get("consultId").toString());
			}

			caseService.createCaseManual(caseVO, consultId);
			return ResponseEntity.ok(Map.of("success", true));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "사건 등록 실패"));
		}
	}
}