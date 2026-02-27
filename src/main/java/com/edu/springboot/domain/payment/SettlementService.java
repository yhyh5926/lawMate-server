//package com.edu.springboot.domain.payment;
//
//import com.edu.springboot.domain.consult.ConsultMapper;
//import com.edu.springboot.domain.consult.vo.ConsultVO;
//import com.edu.springboot.domain.payment.vo.PaymentVO;
//import com.edu.springboot.domain.payment.vo.SettlementVO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class SettlementService {
//
//    private static final double FEE_RATE = 0.1;
//
//    // SettlementMapper 없음 - PaymentMapper 하나에 결제+정산 메서드 모두 포함
//    private final PaymentMapper paymentMapper;
//    private final ConsultMapper consultMapper;
//
//    @Transactional
//    public PaymentVO createOrder(Long memberNo, Long consultNo) {
//        ConsultVO consult = consultMapper.selectConsultByNo(consultNo);
//        if (consult == null) throw new IllegalArgumentException("예약을 찾을 수 없습니다.");
//        if (!"CONFIRMED".equals(consult.getStatus()))
//            throw new IllegalStateException("확정된 예약만 결제 가능합니다.");
//
//        long amount = consult.getConsultFee() * consult.getDuration() / 60;
//        String merchantUid = "LAWMATE-"
//                + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
//
//        PaymentVO vo = PaymentVO.builder()
//                .consultNo(consultNo).memberNo(memberNo)
//                .merchantUid(merchantUid).amount(amount).status("READY")
//                .build();
//        paymentMapper.insertPayment(vo);
//        return vo;
//    }
//
//    @Transactional
//    public void verifyPayment(String impUid, String merchantUid) {
//        PaymentVO payment = paymentMapper.selectByMerchantUid(merchantUid);
//        if (payment == null) throw new IllegalArgumentException("주문을 찾을 수 없습니다.");
//        // TODO: 포트원 REST API 실금액 검증
//        paymentMapper.updatePaid(merchantUid, impUid, "CARD");
//        consultMapper.updatePaidYn(payment.getConsultNo());
//        ConsultVO consult = consultMapper.selectConsultByNo(payment.getConsultNo());
//        long fee = (long)(payment.getAmount() * FEE_RATE);
//        paymentMapper.insertSettlement(SettlementVO.builder()
//                .lawyerNo(consult.getLawyerNo()).paymentNo(payment.getPaymentNo())
//                .consultDate(consult.getConsultDate()).memberName(consult.getMemberName())
//                .paymentAmount(payment.getAmount()).fee(fee)
//                .settlementAmount(payment.getAmount() - fee).status("PENDING")
//                .build());
//    }
//
//    @Transactional
//    public void refund(Long paymentNo, Long memberNo, String reason) {
//        PaymentVO payment = paymentMapper.selectByPaymentNo(paymentNo);
//        if (payment == null) throw new IllegalArgumentException("결제를 찾을 수 없습니다.");
//        if (!payment.getMemberNo().equals(memberNo)) throw new SecurityException("권한 없음");
//        if (!"PAID".equals(payment.getStatus())) throw new IllegalStateException("환불 불가 상태");
//        // TODO: 포트원 환불 API 호출
//        paymentMapper.updateCancelled(paymentNo, reason);
//        consultMapper.updateStatus(payment.getConsultNo(), "CANCELLED");
//    }
//
//    public List<SettlementVO> getSettlementList(Long lawyerNo, int year, int month) {
//        return paymentMapper.selectSettlementList(lawyerNo, year, month);
//    }
//
//    public List<SettlementVO> getMonthlyChart(Long lawyerNo) {
//        return paymentMapper.selectMonthlyChart(lawyerNo);
//    }
//}
