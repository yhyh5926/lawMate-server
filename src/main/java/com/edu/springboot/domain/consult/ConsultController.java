package com.edu.springboot.domain.consult;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
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

        Long memberNo = getMemberNo(bearer);
        vo.setMemberNo(memberNo);

        // 중복 시간 체크
        List<String> booked = consultMapper.selectBookedTimes(
                vo.getLawyerNo(), vo.getConsultDate());
        if (booked.contains(vo.getConsultTime())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("이미 예약된 시간입니다."));
        }

        consultMapper.insertConsult(vo);
        return ResponseEntity.ok(ApiResponse.success(vo));
    }

    /** 내 예약 목록 */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ConsultVO>>> myList(
            @RequestHeader("Authorization") String bearer,
            @RequestParam(required = false) String status) {

        Long memberNo = getMemberNo(bearer);
        List<ConsultVO> list = (status == null || status.isBlank())
                ? consultMapper.selectConsultListByMember(memberNo)
                : consultMapper.selectConsultListByMemberAndStatus(memberNo, status);

        return ResponseEntity.ok(ApiResponse.success(list));
    }

    /** 예약 단건 조회 */
    @GetMapping("/{consultNo}")
    public ResponseEntity<ApiResponse<ConsultVO>> detail(
            @PathVariable Long consultNo) {

        ConsultVO vo = consultMapper.selectConsultByNo(consultNo);
        if (vo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(vo));
    }

    /** 예약 취소 */
    @PutMapping("/{consultNo}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @PathVariable Long consultNo,
            @RequestHeader("Authorization") String bearer) {

        Long memberNo = getMemberNo(bearer);
        ConsultVO vo  = consultMapper.selectConsultByNo(consultNo);

        if (vo == null) {
            return ResponseEntity.notFound().build();
        }
        if (!vo.getMemberNo().equals(memberNo)) {
            return ResponseEntity.status(403).body(ApiResponse.fail("권한이 없습니다."));
        }
        if ("DONE".equals(vo.getStatus()) || "CANCELLED".equals(vo.getStatus())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("취소할 수 없는 상태입니다."));
        }

        consultMapper.updateStatus(consultNo, "CANCELLED");
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /** 변호사 가용 시간 조회 */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<String>>> available(
            @RequestParam Long   lawyerNo,
            @RequestParam String date) {

        List<String> booked    = consultMapper.selectBookedTimes(lawyerNo, date);
        List<String> available = new ArrayList<>();
        DateTimeFormatter fmt  = DateTimeFormatter.ofPattern("HH:mm");

        for (int h = 9; h <= 17; h++) {
            String slot = LocalTime.of(h, 0).format(fmt);
            if (!booked.contains(slot)) {
                available.add(slot);
            }
        }
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    // ── 헬퍼 ────────────────────────────────────────────────
    private Long getMemberNo(String bearer) {
        return jwtUtil.getMemberNo(bearer.replace("Bearer ", ""));
    }
}
