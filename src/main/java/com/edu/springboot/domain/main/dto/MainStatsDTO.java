package com.edu.springboot.domain.main.dto;

import lombok.Data;

import java.util.List;

@Data
public class MainStatsDTO {
    /** 오늘 접수 사건 수 */
    private int todayCount;

    /** 주간 누적 상담 건수 */
    private int weeklyCount;

    /** 차트용 일자별 사건 수 (YYYY-MM-DD, count) */
    private List<MainSeriesPointDTO> series;
}
