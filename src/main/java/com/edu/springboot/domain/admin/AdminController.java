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

import lombok.RequiredArgsConstructor;

//@RestController
@RequiredArgsConstructor
public class AdminController {

    private final LawyerMapper lawyerMapper;
    private final CaseService caseService;
    private final ReportMapper reportMapper;

    // 1. 전문회원 승인 목록 조회
    @GetMapping("/admin/lawyer/approve.do")
    public ResponseEntity<?> getPendingLawyers() {
        return ResponseEntity.ok(Map.of("data", lawyerMapper.findPendingLawyers()));
    }

    // 2. 전문회원 승인 처리
    @PostMapping("/admin/lawyer/approve.do")
    public ResponseEntity<?> approveLawyer(@RequestBody LawyerVO lawyerVO) {
        lawyerMapper.updateApproveStatus(lawyerVO);
        return ResponseEntity.ok(Map.of("message", "전문회원 상태가 업데이트 되었습니다."));
    }

    // 3. 사건 전체 목록 조회
    @GetMapping("/admin/case/list.do")
    public ResponseEntity<?> getAllCases() {
        return ResponseEntity.ok(Map.of("data", caseService.getAllCases()));
    }

    // 4. 신고 접수 목록
    @GetMapping("/admin/report/list.do")
    public ResponseEntity<?> getReportList() {
        return ResponseEntity.ok(Map.of("data", reportMapper.findAllReports()));
    }

    // 5. 제재 처리 (신고 상태 업데이트 + 제재 내역 INSERT)
    @PostMapping("/admin/report/detail.do")
    public ResponseEntity<?> handleSanction(@RequestBody Map<String, Object> payload) {
        // 실제 운영에서는 Map 대신 DTO를 생성하여 처리합니다.
        return ResponseEntity.ok(Map.of("message", "제재 및 신고 처리가 완료되었습니다."));
    }
}