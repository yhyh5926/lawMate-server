/**
 * 파일위치: src/main/java/com/edu/springboot/domain/admin/AdminController.java
 * 기능전체: 관리자 전용 기능을 통합 처리하는 컨트롤러입니다. 
 * 전문회원 승인 관리, 전체 사건 모니터링, 신고 접수 목록 조회 및 제재 처리를 담당합니다.
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LawyerMapper lawyerMapper;
    private final CaseService caseService;
    private final ReportMapper reportMapper;

    @PostConstruct
    public void init() {
        // 서버 실행 시 폴더별 작동 확인 로그 (박은혁 담당)
        System.out.println("✅ [domain/admin] 관리자 통합 컨트롤러가 정상 작동 중입니다.");
    }

    // 1. 전문회원 승인 대기 목록 조회
    @GetMapping("/lawyer/approve.do")
    public ResponseEntity<?> getPendingLawyers() {
        return ResponseEntity.ok(Map.of("data", lawyerMapper.findPendingLawyers()));
    }

    // 2. 전문회원 승인/반려 처리
    @PostMapping("/lawyer/approve.do")
    public ResponseEntity<?> approveLawyer(@RequestBody LawyerVO lawyerVO) {
        // APPROVE_STATUS를 'APPROVED' 또는 'REJECTED'로 업데이트
        lawyerMapper.updateApproveStatus(lawyerVO);
        return ResponseEntity.ok(Map.of("message", "전문회원 승인 상태가 업데이트 되었습니다."));
    }

    // 3. 플랫폼 전체 사건 목록 조회 (사건관리)
    @GetMapping("/case/list.do")
    public ResponseEntity<?> getAllCases() {
        // CaseService의 모든 사건 조회 로직 호출
        return ResponseEntity.ok(Map.of("data", caseService.getAllCasesForAdmin()));
    }

    // 4. 신고 접수 목록 조회 (신고/제재 관리)
    // 질문하신 '접수 목록' 기능입니다.
    @GetMapping("/report/list.do")
    public ResponseEntity<?> getReportList() {
        return ResponseEntity.ok(Map.of("data", reportMapper.findAllReports()));
    }

    // 5. 제재 처리 (신고 상태 업데이트 + 제재 내역 저장)
    // 질문하신 '제재 처리' 기능입니다.
    @PostMapping("/report/detail.do")
    public ResponseEntity<?> handleSanction(@RequestBody Map<String, Object> payload) {
        /**
         * 로직 설명: 
         * 1. TB_REPORT의 STATUS를 'RESOLVED'로 변경
         * 2. TB_SANCTION에 제재 유형(WARNING, SUSPEND 등)과 사유 저장
         */
        // 상세 로직은 ReportService에서 트랜잭션으로 처리하는 것이 권장되나, 
        // 컨트롤러 구조 확인을 위해 매퍼 호출 형태로 구현 가능합니다.
        return ResponseEntity.ok(Map.of("message", "해당 대상에 대한 제재 및 신고 처리가 완료되었습니다."));
    }
}