/**
 * 파일위치: src/main/java/com/edu/springboot/domain/report/ReportController.java
 * 기능전체: 사용자 신고 접수 API를 담당합니다. (관리자 조회는 AdminController에서 처리)
 */
package com.edu.springboot.domain.report;

import com.edu.springboot.domain.report.vo.ReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 일반 사용자의 신고 등록 (게시글, 댓글, 리뷰 등)
    @PostMapping("/submit.do")
    public ResponseEntity<?> submitReport(@RequestBody ReportVO reportVO) {
        boolean success = reportService.registerReport(reportVO);
        return ResponseEntity.ok(Map.of("success", success));
    }
}