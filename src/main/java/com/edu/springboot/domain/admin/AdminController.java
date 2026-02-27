/**
 * 파일위치: src/main/java/com/edu/springboot/domain/admin/AdminController.java
 * 수정사항: 모든 컨트롤러의 경로를 /api로 통일하여 프론트엔드 통신 오류를 방지합니다.
 */
package com.edu.springboot.domain.admin;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.edu.springboot.domain.cases.CaseService;
import com.edu.springboot.domain.lawyer.LawyerMapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;
import com.edu.springboot.domain.report.ReportMapper;
import com.edu.springboot.domain.report.ReportMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin") // 💡 /api 추가
@RequiredArgsConstructor
public class AdminController {

    private final LawyerMapper lawyerMapper;
    private final CaseService caseService;
    private final ReportMapper reportMapper;

    @PostConstruct
    public void init() {
        System.out.println("✅ [domain/admin] 관리자 컨트롤러가 /api/admin 경로로 가동됩니다.");
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

    @GetMapping("/report/list.do")
    public ResponseEntity<?> getReportList() {
        return ResponseEntity.ok(Map.of("data", reportMapper.findAllReports()));
    }

    @PostMapping("/report/detail.do")
    public ResponseEntity<?> handleSanction(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(Map.of("message", "처리 완료"));
    }
}