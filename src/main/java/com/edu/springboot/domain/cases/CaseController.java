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

	// 💡 3. [수정됨] 사건 진행 상태(STEP) 업데이트 (400 에러 해결을 위해 문자열 직접 수신)
	@PutMapping("/{caseId}/status")
	public ResponseEntity<?> updateCaseStatus(@PathVariable("caseId") Long caseId,
			@RequestBody Map<String, String> payload) {
		// 프론트엔드에서 보낸 문자열 상태값(RECEIVED, IN_PROGRESS 등)을 직접 가져옵니다.
		String statusKey = payload.get("status");
		if (statusKey != null) {
			caseService.updateCaseStep(caseId, statusKey);
		}
		return ResponseEntity.ok(Map.of("success", true));
	}

	// 💡 4. [400 에러 해결] 필수 파라미터 체크를 해제(required=false)하여 빈 값이 들어와도 허용합니다.
	@PutMapping("/{caseId}")
	public ResponseEntity<?> updateCaseInfo(@PathVariable("caseId") Long caseId,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "expertOpinion", required = false) String expertOpinion,
			@RequestParam(value = "files", required = false) List<MultipartFile> files) {

		CaseVO caseVO = new CaseVO();
		caseVO.setCaseId(caseId);
		// null 방지 처리
		caseVO.setDescription(description != null ? description : "");
		caseVO.setExpertOpinion(expertOpinion != null ? expertOpinion : "");

		caseService.updateCaseInfoWithFiles(caseVO, files);
		return ResponseEntity.ok(Map.of("success", true));
	}

	// 💡 5. 의뢰인 사건 완료 후기 등록
	@PostMapping("/{caseId}/review")
	public ResponseEntity<?> submitReview(@PathVariable("caseId") Long caseId,
			@RequestBody Map<String, Object> payload) {
		return ResponseEntity.ok(Map.of("success", true));
	}

	// 💡 6. 변호사가 마이페이지에서 수동으로 사건 등록
	@PostMapping("/manual-register")
	public ResponseEntity<?> createCaseManual(@RequestBody CaseVO caseVO) {
		// 새로 등록되는 사건이므로 기본 상태를 'RECEIVED(접수)'로 고정
		caseVO.setStep("RECEIVED");
		caseService.createCaseManual(caseVO);
		return ResponseEntity.ok(Map.of("success", true));
	}
}