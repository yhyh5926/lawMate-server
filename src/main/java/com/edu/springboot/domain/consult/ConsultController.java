package com.edu.springboot.domain.consult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.springboot.common.jwt.JwtUtil;
import com.edu.springboot.common.response.ApiResponse;
import com.edu.springboot.domain.consult.vo.ConsultVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/consult")
@RequiredArgsConstructor
public class ConsultController {

    private final ConsultMapper consultMapper;
    private final JwtUtil       jwtUtil;

    /** 상담 예약 생성 */
    @PostMapping
    public ResponseEntity<ApiResponse<ConsultVO>> create(
            @RequestHeader("Authorization") String bearer,
            @RequestBody ConsultVO vo) {

        Long memberId = getMemberId(bearer);
        vo.setMemberId(memberId);

        // 중복 날짜 체크
        List<String> booked = consultMapper.selectBookedDates(vo.getLawyerId());
        if (booked.contains(vo.getConsultDate())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("이미 예약된 날짜입니다."));
        }

        consultMapper.insertConsult(vo);
        return ResponseEntity.ok(ApiResponse.success(vo));
    }

    /** 내 예약 목록 */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ConsultVO>>> myList(
            @RequestHeader("Authorization") String bearer,
            @RequestParam(value = "status", required = false) String status) {

        Long memberId = getMemberId(bearer);
        List<ConsultVO> list = (status == null || status.isBlank())
                ? consultMapper.selectConsultListByMember(memberId)
                : consultMapper.selectConsultListByMemberAndStatus(memberId, status);

        return ResponseEntity.ok(ApiResponse.success(list));
    }

    /** 예약 단건 조회 */
    @GetMapping("/{consultId}")
    public ResponseEntity<ApiResponse<ConsultVO>> detail(
    		@PathVariable("consultId") Long consultId) {

        ConsultVO vo = consultMapper.selectConsultByNo(consultId);
        if (vo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(vo));
    }

    /** 예약 취소 */
    @PutMapping("/{consultId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(
    		@PathVariable("consultId") Long consultId,
            @RequestHeader("Authorization") String bearer) {

        Long memberId = getMemberId(bearer);
        ConsultVO vo  = consultMapper.selectConsultByNo(consultId);

        if (vo == null) {
            return ResponseEntity.notFound().build();
        }
        if (!vo.getMemberId().equals(memberId)) {
            return ResponseEntity.status(403).body(ApiResponse.fail("권한이 없습니다."));
        }
        if ("DONE".equals(vo.getStatus()) || "CANCELLED".equals(vo.getStatus())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("취소할 수 없는 상태입니다."));
        }

        consultMapper.updateStatus(consultId, "CANCELLED");
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /** 변호사 가용 날짜 조회 (오늘부터 30일) */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<String>>> available(
            @RequestParam(value = "lawyerId") Long lawyerId) {

        List<String> booked    = consultMapper.selectBookedDates(lawyerId);
        List<String> available = new ArrayList<>();
        DateTimeFormatter fmt  = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < 30; i++) {
            String date = LocalDate.now().plusDays(i).format(fmt);
            if (!booked.contains(date)) {
                available.add(date);
            }
        }
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    // ── 헬퍼 ────────────────────────────────────────────────
    private Long getMemberId(String bearer) {
        return jwtUtil.getMemberNo(bearer.replace("Bearer ", ""));
    }
    
    /** 복구 */
    @PutMapping("/{consultId}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable("consultId") Long consultId,
            @RequestHeader("Authorization") String bearer) {
        Long memberId = getMemberId(bearer);
        ConsultVO vo = consultMapper.selectConsultByNo(consultId);
        if (vo == null) return ResponseEntity.notFound().build();
        if (!vo.getMemberId().equals(memberId))
            return ResponseEntity.status(403).body(ApiResponse.fail("권한이 없습니다."));
        consultMapper.restoreConsult(consultId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /** 즉시 삭제 */
    @DeleteMapping("/{consultId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable("consultId") Long consultId,
            @RequestHeader("Authorization") String bearer) {
        Long memberId = getMemberId(bearer);
        ConsultVO vo = consultMapper.selectConsultByNo(consultId);
        if (vo == null) return ResponseEntity.notFound().build();
        if (!vo.getMemberId().equals(memberId))
            return ResponseEntity.status(403).body(ApiResponse.fail("권한이 없습니다."));
        consultMapper.deleteConsult(consultId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    
    /** 변호사 - 접수된 상담 목록 */
    @GetMapping("/lawyer")
    public ResponseEntity<ApiResponse<List<ConsultVO>>> lawyerList(
            @RequestHeader("Authorization") String bearer) {
        Long memberId = getMemberId(bearer);
        Long lawyerId = consultMapper.selectLawyerIdByMemberId(memberId);
        if (lawyerId == null)
            return ResponseEntity.status(403).body(ApiResponse.fail("변호사 계정이 아닙니다."));
        List<ConsultVO> list = consultMapper.selectConsultListByLawyer(lawyerId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    /** 변호사 - 승인 */
    @PutMapping("/{consultId}/confirm")
    public ResponseEntity<ApiResponse<Void>> confirm(
            @PathVariable("consultId") Long consultId,
            @RequestHeader("Authorization") String bearer) {
        Long memberId = getMemberId(bearer);
        Long lawyerId = consultMapper.selectLawyerIdByMemberId(memberId);
        ConsultVO vo = consultMapper.selectConsultByNo(consultId);
        if (vo == null) return ResponseEntity.notFound().build();
        if (!vo.getLawyerId().equals(lawyerId))
            return ResponseEntity.status(403).body(ApiResponse.fail("권한이 없습니다."));
        consultMapper.updateStatus(consultId, "CONFIRMED");
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /** 변호사 - 거절 */
    @PutMapping("/{consultId}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(
            @PathVariable("consultId") Long consultId,
            @RequestHeader("Authorization") String bearer) {
        Long memberId = getMemberId(bearer);
        Long lawyerId = consultMapper.selectLawyerIdByMemberId(memberId);
        ConsultVO vo = consultMapper.selectConsultByNo(consultId);
        if (vo == null) return ResponseEntity.notFound().build();
        if (!vo.getLawyerId().equals(lawyerId))
            return ResponseEntity.status(403).body(ApiResponse.fail("권한이 없습니다."));
        consultMapper.updateStatus(consultId, "CANCELLED");
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}