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
    private Long          consultId;     // CONSULT_ID
    private Long          memberId;      // MEMBER_ID
    private Long          lawyerId;      // LAWYER_ID
    private Long          caseId;        // CASE_ID
    private String        consultDate;   // CONSULT_DATE (yyyy-MM-dd)
    private int           durationMin;   // DURATION_MIN
    private String        note;          // NOTE
    /**
     * PENDING   : 예약 대기
     * CONFIRMED : 확정
     * DONE      : 완료
     * CANCELLED : 취소
     */
    private String        status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* JOIN 결과 */
    private String        lawyerName;
    private String        memberName;
    private String        memberPhone;
    private Long          consultFee;
    
    private boolean reviewed;
    private String rejectReason;
    
}