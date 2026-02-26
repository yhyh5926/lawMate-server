/**
 * 파일위치: src/main/java/com/edu/springboot/domain/question/vo/QuestionVO.java
 * 기능전체: TB_QUESTION 테이블과 매핑되는 법률 질문 데이터 객체입니다.
 */
package com.edu.springboot.domain.question.vo;

import lombok.Data;
import java.util.Date;

@Data
public class QuestionVO {
    private Long questionId;      // 질문 고유 번호 (PK)
    private Long memberId;        // 질문자 회원 번호 (FK)
    private Long caseId;          // 연결 사건 번호 (선택)
    private String title;         // 질문 제목
    private String content;       // 질문 상세 내용
    private String caseType;      // 사건 유형
    private Long assignedLawyer;  // 답변 담당 변호사 번호
    private String status;        // 상태 (OPEN/ANSWERED/CLOSED)
    private Date createdAt;       // 등록 일시
    private Date updatedAt;       // 수정 일시
}