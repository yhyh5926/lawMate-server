package com.edu.springboot.domain.payment;

import com.edu.springboot.domain.payment.vo.PaymentVO;
import com.edu.springboot.domain.payment.vo.SettlementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentMapper {

    // ── 결제 ──────────────────────────────────────────────

    /** 주문 생성 (READY 상태) */
    int insertPayment(PaymentVO vo);

    /** merchantUid 로 조회 */
    PaymentVO selectByMerchantUid(@Param("merchantUid") String merchantUid);

    /** paymentNo 로 조회 */
    PaymentVO selectByPaymentNo(@Param("paymentNo") Long paymentNo);

    /** 회원 결제 목록 */
    List<PaymentVO> selectPaymentListByMember(@Param("memberNo") Long memberNo);

    /** 결제 완료 처리 */
    int updatePaid(@Param("merchantUid") String merchantUid,
                   @Param("impUid")      String impUid,
                   @Param("payMethod")   String payMethod);

    /** 환불 처리 */
    int updateCancelled(@Param("paymentNo")    Long paymentNo,
                        @Param("refundReason") String refundReason);

    // ── 정산 ──────────────────────────────────────────────

    /** 정산 레코드 생성 */
    int insertSettlement(SettlementVO vo);

    /** 정산 목록 (연/월 필터) */
    List<SettlementVO> selectSettlementList(@Param("lawyerNo") Long lawyerNo,
                                             @Param("year")     int  year,
                                             @Param("month")    int  month);

    /** 월별 차트 데이터 (최근 12개월) */
    List<SettlementVO> selectMonthlyChart(@Param("lawyerNo") Long lawyerNo);

    /** 정산 완료 처리 */
    int updateComplete(@Param("settlementNo") Long settlementNo);
}
