package com.edu.springboot.domain.answer.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * [사건관리] 변호사 답변 정보 VO
 * 테이블명: TB_ANSWER
 */
@Data
public class AnswerVO {

    private Long answerId;
    private Long questionId;
    private Long lawyerId;
    private String content;
    private String isAdopted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* --- 조인 조회를 위한 추가 필드 --- */
    private String lawyerName;      // 답변 작성 변호사 이름
    private String officeName;      // 소속 사무소
    private String specialty;       // 전문 분야
    private String lawyerProfile;   // 변호사 프로필 사진 경로

    // 💡 [추가] 변호사 신뢰도 및 활동 지표 (React에서 사용됨)
    private int adoptCnt;           // 해당 변호사의 총 채택 수
    private int answerCnt;          // 해당 변호사의 총 답변 수

    /**
     * 💡 [추가] 채택률 계산 로직
     * 프론트엔드에서 편하게 쓸 수 있도록 VO 단계에서 계산된 값을 제공합니다.
     */
    public double getAdoptRate() {
        if (this.answerCnt == 0) return 0.0;
        double rate = ((double) this.adoptCnt / this.answerCnt) * 100;
        return Math.round(rate * 10.0) / 10.0; // 소수점 첫째 자리 반올림
    }
}