// IntelliJ 또는 Eclipse (백엔드)
// 파일위치: src/main/java/com/edu/springboot/domain/cases/CaseController.java

package com.edu.springboot.domain.cases;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseController {

	private final CaseService caseService;

	@PostConstruct
	public void init() {
		System.out.println("✅ [domain/cases] 사건 관리 모듈이 활성화되었습니다.");
	}

	// 내 사건 목록 조회
	@GetMapping("/list")
	public ResponseEntity<?> getMyCaseList(@RequestParam("memberId") Long memberId) {
		return ResponseEntity.ok(caseService.getCaseListByMember(memberId));
	}

	// 사건 상세 조회
	@GetMapping("/detail")
	public ResponseEntity<?> getCaseDetail(@RequestParam("caseId") Long caseId) {
		return ResponseEntity.ok(caseService.getCaseDetail(caseId));
	}
}