// src/main/java/com/edu/springboot/domain/admin/AdminController.java
package com.edu.springboot.domain.admin;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.springboot.domain.cases.CaseService;
import com.edu.springboot.domain.lawyer.LawyerMapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;
import com.edu.springboot.domain.member.MemberMapper;
import com.edu.springboot.domain.report.ReportMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LawyerMapper lawyerMapper;
    private final CaseService caseService;
    private final ReportMapper reportMapper;
    private final MemberMapper memberMapper; // DB 연동을 위해 추가

    @PostConstruct
    public void init() {
        System.out.println("✅ [domain/admin] 관리자 컨트롤러가 /api/admin 경로로 가동됩니다.");
    }

    // [회원통합관리] 전체 회원 목록 조회 (더미데이터 제거 및 DB 연동)
    @GetMapping("/member/list.do")
    public ResponseEntity<?> getMemberList() {
        return ResponseEntity.ok(Map.of("data", memberMapper.selectAllMembers()));
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

    // Report Detail 제재 처리 로직 등 기타 기존 메서드 유지...
}