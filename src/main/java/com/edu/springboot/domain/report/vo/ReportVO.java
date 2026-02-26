/**
 * 파일위치: src/main/java/com/edu/springboot/domain/report/vo/ReportVO.java
 * 기능전체: TB_REPORT 테이블 매핑 객체입니다. (사용자 신고 정보)
 */
package com.edu.springboot.domain.report.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportVO {
    private Long reportId;      // 신고 고유 번호 (PK)
    private Long reporterId;    // 신고자 회원 번호 (FK)
    private String targetType;  // 대상 유형 (MEMBER/POST/COMMENT/REVIEW)
    private Long targetId;      // 대상 PK
    private String reason;      // 신고 사유 (분류)
    private String detail;      // 상세 내용
    private String status;      // 처리 상태 (PENDING/RESOLVED/DISMISSED)
    private Long handledBy;     // 처리 관리자 ID
    private Date handledAt;     // 처리 일시
    private String resultNote;  // 처리 결과 메모
    private Date createdAt;     // 접수 일시
}