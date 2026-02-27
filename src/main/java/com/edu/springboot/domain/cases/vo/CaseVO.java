/**
 * 파일위치: src/main/java/com/edu/springboot/domain/cases/vo/CaseVO.java
 * 기능전체: TB_CASE 테이블과 1:1 매핑되는 사건 정보 데이터 객체입니다.
 */
package com.edu.springboot.domain.cases.vo;

import lombok.Data;
import java.util.Date;

@Data
public class CaseVO {
    private Long caseId;         // 사건 고유 번호 (PK)
    private Long memberId;       // 의뢰인 회원 번호 (FK)
    private Long lawyerId;       // 담당 변호사 번호 (FK)
    private String title;        // 사건 제목
    private String caseType;     // 사건 유형 (민사/형사 등)
    private String description;  // 상세 내용
    private String step;         // 진행 단계 (RECEIVED/ASSIGNED/IN_PROGRESS 등)
    private String expertOpinion;// 전문가 의견서
    private Date createdAt;      // 접수일시
    private Date updatedAt;      // 수정일시
    private Date closedAt;       // 종료일시
}