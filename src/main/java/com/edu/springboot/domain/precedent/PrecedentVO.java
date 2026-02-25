package com.edu.springboot.domain.precedent;

import lombok.Data;
import java.util.Date;

@Data
public class PrecedentVO {
    private Long precId;          // PREC_ID
    private String caseNo;        // CASE_NO
    private String court;         // COURT
    private String judgeDate;     // JUDGE_DATE (DB에서는 DATE지만 VO에서는 String이 편리할 수 있음)
    private String caseType;      // CASE_TYPE
    private String title;         // TITLE
    private String oneLine;       // ONE_LINE
    private String judgment;      // JUDGMENT
    private String aiSummary;     // AI_SUMMARY (CLOB)
    private String keywordCsv;    // KEYWORD_CSV
    private Date createdAt;       // CREATED_AT
}