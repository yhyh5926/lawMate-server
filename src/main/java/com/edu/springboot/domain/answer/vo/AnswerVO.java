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
    private String lawyerName;     // 답변 작성 변호사 이름
    private String officeName;     // 소속 사무소
    private String specialty;      // 전문 분야
    private String lawyerProfile;  // 변호사 프로필 사진 경로
}