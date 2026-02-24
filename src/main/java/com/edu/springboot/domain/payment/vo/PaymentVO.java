package com.edu.springboot.domain.payment.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentVO {

    private Long          paymentNo;
    private Long          consultNo;
    private Long          memberNo;
    private String        impUid;          // 포트원 imp_uid
    private String        merchantUid;     // 서버 주문번호
    private Long          amount;
    private String        payMethod;       // CARD | TRANSFER | KAKAOPAY

    /**
     * READY     : 결제 준비
     * PAID      : 결제 완료
     * CANCELLED : 환불
     * FAILED    : 실패
     */
    private String        status;
    private String        refundReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
