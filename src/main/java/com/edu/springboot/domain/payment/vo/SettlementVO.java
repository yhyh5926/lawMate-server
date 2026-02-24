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
public class SettlementVO {

    private Long          settlementNo;
    private Long          lawyerNo;
    private Long          paymentNo;
    private String        consultDate;      // yyyy-MM-dd
    private String        memberName;
    private Long          paymentAmount;    // 결제 원금
    private Long          fee;              // 수수료 (10%)
    private Long          settlementAmount; // 실 정산액

    /**
     * PENDING   : 정산 대기
     * COMPLETED : 정산 완료
     */
    private String        status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* 차트 집계용 */
    private String        month;            // 'YYYY-MM'
}
