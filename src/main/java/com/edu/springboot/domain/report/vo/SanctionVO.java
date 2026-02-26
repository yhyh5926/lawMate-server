/**
 * 파일위치: src/main/java/com/edu/springboot/domain/report/vo/SanctionVO.java
 * 기능전체: TB_SANCTION 테이블 매핑 객체입니다. (관리자 제재 이력 정보)
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
public class SanctionVO {
    private Long sanctionId;     // 제재 고유 번호 (PK)
    private Long memberId;       // 대상 회원 번호 (FK)
    private Long reportId;       // 연관 신고 번호 (FK, NULL 가능)
    private String sanctionType; // 유형 (WARNING/SUSPEND/FORCE_WITHDRAW)
    private String reason;       // 제재 사유
    private Date startDate;      // 제재 시작일
    private Date endDate;        // 제재 종료일 (NULL=영구)
    private Long adminId;        // 처리 관리자 ID
    private Date createdAt;      // 등록 일시
}