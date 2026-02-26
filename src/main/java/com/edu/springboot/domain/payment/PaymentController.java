//package com.edu.springboot.domain.payment;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.edu.springboot.common.jwt.JwtUtil;
//import com.edu.springboot.common.response.ApiResponse;
//import com.edu.springboot.domain.payment.vo.PaymentVO;
//import com.edu.springboot.domain.payment.vo.SettlementVO;
//
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/api/payment")
//@RequiredArgsConstructor
//public class PaymentController {
//
//    private final SettlementService settlementService;
//    private final JwtUtil           jwtUtil;
//
//    /** 주문 생성 (결제창 오픈 전) */
//    @PostMapping("/order")
//    public ResponseEntity<ApiResponse<PaymentVO>> createOrder(
//            @RequestHeader("Authorization") String bearer,
//            @RequestBody Map<String, Long> body) {
//
//        Long memberNo = getMemberNo(bearer);
//        PaymentVO result = settlementService.createOrder(memberNo, body.get("consultNo"));
//        return ResponseEntity.ok(ApiResponse.success(result));
//    }
//
//    /** PG 결제 완료 검증 */
//    @PostMapping("/verify")
//    public ResponseEntity<ApiResponse<Void>> verify(
//            @RequestBody Map<String, String> body) {
//
//        settlementService.verifyPayment(body.get("impUid"), body.get("merchantUid"));
//        return ResponseEntity.ok(ApiResponse.success(null));
//    }
//
//    /** 환불 신청 */
//    @PostMapping("/{paymentNo}/refund")
//    public ResponseEntity<ApiResponse<Void>> refund(
//            @PathVariable Long paymentNo,
//            @RequestHeader("Authorization") String bearer,
//            @RequestBody Map<String, String> body) {
//
//        Long memberNo = getMemberNo(bearer);
//        settlementService.refund(paymentNo, memberNo, body.get("reason"));
//        return ResponseEntity.ok(ApiResponse.success(null));
//    }
//
//    /** 정산 목록 (전문회원) */
//    @GetMapping("/settlement")
//    public ResponseEntity<ApiResponse<List<SettlementVO>>> settlementList(
//            @RequestHeader("Authorization") String bearer,
//            @RequestParam int year,
//            @RequestParam int month) {
//
//        Long lawyerNo = getMemberNo(bearer); // 실제로는 lawyerNo 별도 조회 필요
//        return ResponseEntity.ok(
//                ApiResponse.success(
//                        settlementService.getSettlementList(lawyerNo, year, month)));
//    }
//
//    /** 월별 정산 차트 */
//    @GetMapping("/settlement/chart")
//    public ResponseEntity<ApiResponse<List<SettlementVO>>> settlementChart(
//            @RequestHeader("Authorization") String bearer) {
//
//        Long lawyerNo = getMemberNo(bearer);
//        return ResponseEntity.ok(
//                ApiResponse.success(settlementService.getMonthlyChart(lawyerNo)));
//    }
//
//    // ── 헬퍼 ────────────────────────────────────────────────
//    private Long getMemberNo(String bearer) {
//        return jwtUtil.getMemberNo(bearer.replace("Bearer ", ""));
//    }
//}
