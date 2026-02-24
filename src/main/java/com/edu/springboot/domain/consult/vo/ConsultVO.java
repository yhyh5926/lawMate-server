package com.edu.springboot.domain.consult.vo;

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
public class ConsultVO {

    private Long          consultNo;
    private Long          memberNo;
    private Long          lawyerNo;
    private String        consultDate;   // yyyy-MM-dd
    private String        consultTime;   // HH:mm
    private int           duration;      // 분
    private String        memo;

    /**
     * PENDING   : 예약 대기
     * CONFIRMED : 확정
     * DONE      : 완료
     * CANCELLED : 취소
     */
    private String        status;
    private String        paidYn;        // Y/N
    private String        reviewedYn;    // Y/N
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* JOIN 결과 */
    private String        lawyerName;
    private String        memberName;
    private String        memberPhone;
    private Long          consultFee;
}
