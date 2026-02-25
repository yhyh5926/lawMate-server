package com.edu.springboot.domain.question.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuestionVO {
    private Long questionId;
    private Long memberId;
    private Long caseId;
    private String title;
    private String content;
    private String caseType;
    private Long assignedLawyer;
    private String status; // OPEN / ANSWERED / CLOSED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}