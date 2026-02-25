package com.edu.springboot.domain.cases;

import com.edu.springboot.domain.cases.vo.CaseVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    // 마이페이지: 의뢰인 사건 목록
    @GetMapping("/mypage/case/list.do")
    public ResponseEntity<?> getMyCaseList(@RequestParam Long memberId) {
        List<CaseVO> caseList = caseService.getMyCases(memberId);
        return ResponseEntity.ok(Map.of("message", "사건 목록 조회 성공", "data", caseList));
    }

    // 마이페이지: 사건 상세 내역
    @GetMapping("/mypage/case/detail.do")
    public ResponseEntity<?> getCaseDetail(@RequestParam Long caseId) {
        CaseVO caseDetail = caseService.getCaseDetail(caseId);
        return ResponseEntity.ok(Map.of("message", "사건 상세 조회 성공", "data", caseDetail));
    }
}