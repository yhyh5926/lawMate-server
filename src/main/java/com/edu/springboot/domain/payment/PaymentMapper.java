package com.edu.springboot.domain.payment;

import com.edu.springboot.domain.payment.vo.PaymentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentMapper {

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
}
