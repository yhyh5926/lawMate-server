/**
 * 파일위치: src/main/java/com/edu/springboot/domain/cases/vo/CaseVO.java
 * 수정사항: DB 정의서(TB_CASE)에 명시된 컬럼 구조와 일치하도록 필드를 구성했습니다.
 */
package com.edu.springboot.domain.cases.vo;

import lombok.Data;
import java.util.Date;

@Data
public class CaseVO {
    private Long caseId;         // CASE_ID (PK)
    private Long memberId;       // MEMBER_ID (FK)
    private Long lawyerId;       // LAWYER_ID (FK)
    private String title;        // TITLE
    private String caseType;     // CASE_TYPE
    private String description;  // DESCRIPTION
    private String step;         // STEP (RECEIVED / ASSIGNED / IN_PROGRESS / OPINION_READY / CLOSED)
    private String expertOpinion;// EXPERT_OPINION
    private Date createdAt;      // CREATED_AT
    private Date updatedAt;      // UPDATED_AT
    private Date closedAt;       // CLOSED_AT
}