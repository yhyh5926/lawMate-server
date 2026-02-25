package com.edu.springboot.domain.cases.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CaseVO {
    private Long caseId;
    private Long memberId;
    private Long lawyerId;
    private String title;
    private String caseType;
    private String description;
    private String step; // RECEIVED / ASSIGNED / IN_PROGRESS / OPINION_READY / CLOSED
    private String expertOpinion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
}