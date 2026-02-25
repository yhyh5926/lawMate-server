package com.edu.springboot.domain.report.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportVO {
    private Long reportId;
    private Long reporterId;
    private String targetType; // MEMBER / POST / COMMENT / REVIEW
    private Long targetId;
    private String reason;
    private String detail;
    private String status; // PENDING / RESOLVED / DISMISSED
    private Long handledBy;
    private LocalDateTime handledAt;
    private String resultNote;
    private LocalDateTime createdAt;
}
