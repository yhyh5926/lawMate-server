package com.edu.springboot.domain.payment;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.springboot.domain.consult.ConsultMapper;
import com.edu.springboot.domain.consult.vo.ConsultVO;
import com.edu.springboot.domain.payment.vo.PaymentVO;
import com.edu.springboot.domain.payment.vo.SettlementVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private static final double FEE_RATE = 0.1; // 수수료 10%

    private final PaymentMapper    paymentMapper;
    private final SettlementMapper settlementMapper;
    private final ConsultMapper    consultMapper;

    /** 주문 생성 (PG 결제창 열기 전 서버 주문번호 발급) */
    @Transactional
    public PaymentVO createOrder(Long memberNo, Long consultNo) {
        ConsultVO consult = consultMapper.selectConsultByNo(consultNo);
        if (consult == null) throw new IllegalArgumentException("예약을 찾을 수 없습니다.");
        if (!"CONFIRMED".equals(consult.getStatus())) {
            throw new IllegalStateException("확정된 예약만 결제 가능합니다.");
        }

        long amount = consult.getConsultFee() * consult.getDuration() / 60;
        String merchantUid = "LAWMATE-"
                + UUID.randomUUID().toString().replace("-", "")
                       .substring(0, 10).toUpperCase();

        PaymentVO vo = PaymentVO.builder()
                .consultNo(consultNo)
                .memberNo(memberNo)
                .merchantUid(merchantUid)
                .amount(amount)
                .status("READY")
                .build();

        paymentMapper.insertPayment(vo);
        return vo;
    }

    /** PG 결제 완료 검증 및 DB 처리 */
    @Transactional
    public void verifyPayment(String impUid, String merchantUid) {
        PaymentVO payment = paymentMapper.selectByMerchantUid(merchantUid);
        if (payment == null) throw new IllegalArgumentException("주문을 찾을 수 없습니다.");

        // TODO: 포트원 REST API(IamportClient)로 impUid → 실결제금액 검증
        //       payment.getAmount() 와 불일치 시 예외 처리

        paymentMapper.updatePaid(merchantUid, impUid, "CARD");
        consultMapper.updatePaidYn(payment.getConsultNo());

        // 정산 레코드 자동 생성
        ConsultVO consult = consultMapper.selectConsultByNo(payment.getConsultNo());
        createSettlementRecord(payment, consult);
    }

    /** 환불 처리 */
    @Transactional
    public void refund(Long paymentNo, Long memberNo, String reason) {
        PaymentVO payment = paymentMapper.selectByPaymentNo(paymentNo);
        if (payment == null) throw new IllegalArgumentException("결제를 찾을 수 없습니다.");
        if (!payment.getMemberNo().equals(memberNo)) throw new SecurityException("권한 없음");
        if (!"PAID".equals(payment.getStatus())) {
            throw new IllegalStateException("환불 가능한 상태가 아닙니다.");
        }

        // TODO: 포트원 환불 API 호출

        paymentMapper.updateCancelled(paymentNo, reason);
        consultMapper.updateStatus(payment.getConsultNo(), "CANCELLED");
    }

    /** 정산 목록 (연/월 필터) */
    public List<SettlementVO> getSettlementList(Long lawyerNo, int year, int month) {
        return settlementMapper.selectSettlementList(lawyerNo, year, month);
    }

    /** 월별 차트 데이터 */
    public List<SettlementVO> getMonthlyChart(Long lawyerNo) {
        return settlementMapper.selectMonthlyChart(lawyerNo);
    }

    // ── 내부 ────────────────────────────────────────────────
    private void createSettlementRecord(PaymentVO payment, ConsultVO consult) {
        long fee = (long) (payment.getAmount() * FEE_RATE);

        SettlementVO settlement = SettlementVO.builder()
                .lawyerNo(consult.getLawyerNo())
                .paymentNo(payment.getPaymentNo())
                .consultDate(consult.getConsultDate())
                .memberName(consult.getMemberName())
                .paymentAmount(payment.getAmount())
                .fee(fee)
                .settlementAmount(payment.getAmount() - fee)
                .status("PENDING")
                .build();

        settlementMapper.insertSettlement(settlement);
    }
}
