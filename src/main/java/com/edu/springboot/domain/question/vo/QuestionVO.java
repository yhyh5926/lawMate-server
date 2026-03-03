/**
 * 파일위치: src/main/java/com/edu/springboot/domain/question/vo/QuestionVO.java
 * 수정사항: DB 정의서(TB_QUESTION)의 ASSIGNED_LAWYER 등 컬럼 정보를 반영했습니다.
 */
package com.edu.springboot.domain.question.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

import com.edu.springboot.domain.answer.vo.AnswerVO;

@Data
public class QuestionVO {
    private Long questionId;      // QUESTION_ID (PK)
    private Long memberId;        // MEMBER_ID (FK)
    private Long caseId;          // CASE_ID (FK, 선택)
    private String title;         // TITLE
    private String content;       // CONTENT
    private String caseType;      // CASE_TYPE
    private Long assignedLawyer;  // ASSIGNED_LAWYER (FK)
    private String status;        // STATUS (OPEN / ANSWERED / CLOSED)
    private Date createdAt;       // CREATED_AT
    private Date updatedAt;       // UPDATED_AT
    
    private List<AnswerVO> answers;
}