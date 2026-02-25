package com.edu.springboot.domain.report.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SanctionVO {
    private Long sanctionId;
    private Long memberId;
    private Long reportId;
    private String sanctionType; // WARNING / SUSPEND / FORCE_WITHDRAW
    private String reason;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long adminId;
    private LocalDateTime createdAt;
}