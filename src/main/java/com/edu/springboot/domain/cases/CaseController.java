// src/main/java/com/edu/springboot/domain/cases/CaseController.java
package com.edu.springboot.domain.cases;

import com.edu.springboot.domain.cases.vo.CaseVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

// 클라이언트의 사건 관련 요청을 처리하는 컨트롤러
@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseController {

	private final CaseService caseService;

//	컨트롤러가 정상적으로 메모리에 올라갔는지 확인하기 위한 초기화 메서드
	@PostConstruct
	public void init() {
		System.out.println("[domain/cases] 사건 관리 컨트롤러가 가동되었습니다.");
	}

//	특정 회원의 내 사건 목록을 조회하는 API
	@GetMapping("/list")
	public ResponseEntity<?> getMyCaseList(@RequestParam("memberId") Long memberId) {
		return ResponseEntity.ok(caseService.getCaseListByMember(memberId));
	}

//	개별 사건의 상세 정보를 조회하는 API
	@GetMapping("/detail")
	public ResponseEntity<?> getCaseDetail(@RequestParam("caseId") Long caseId) {
		return ResponseEntity.ok(caseService.getCaseDetail(caseId));
	}

//	사건의 진행 상태를 다음 단계로 업데이트하는 API
	@PutMapping("/{caseId}/status")
	public ResponseEntity<?> updateCaseStatus(@PathVariable("caseId") Long caseId,
			@RequestBody Map<String, String> payload) {
		String statusKey = payload.get("status");
		if (statusKey != null) {
			caseService.updateCaseStep(caseId, statusKey);
		}
		return ResponseEntity.ok(Map.of("success", true));
	}

//	사건의 내용이나 전문가 의견을 수정하고 첨부파일을 추가하는 API
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

//	의뢰인이 사건 완료 후 변호사에 대한 후기를 등록하는 API
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

//	변호사가 마이페이지에서 수동으로 새로운 사건을 등록하는 API
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