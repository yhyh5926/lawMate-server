/**
 * 파일위치: src/main/java/com/edu/springboot/domain/cases/CaseController.java
 * 기능전체: 사건 등록, 목록 조회, 상태 변경 등 사건 관리와 관련된 HTTP 요청을 처리합니다.
 */
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
    @GetMapping("/list.do")
    public ResponseEntity<?> getMyCaseList(@RequestParam Long memberId) {
        return ResponseEntity.ok(caseService.getCaseListByMember(memberId));
    }

    // 사건 상세 조회
    @GetMapping("/detail.do")
    public ResponseEntity<?> getCaseDetail(@RequestParam Long caseId) {
        return ResponseEntity.ok(caseService.getCaseDetail(caseId));
    }
}