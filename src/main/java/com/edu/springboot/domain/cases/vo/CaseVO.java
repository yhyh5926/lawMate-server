// IntelliJ
// 파일위치: src/main/java/com/edu/springboot/domain/cases/vo/CaseVO.java

package com.edu.springboot.domain.cases.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;
import com.edu.springboot.domain.attachment.vo.AttachmentVO;

@Data
public class CaseVO {
	private Long caseId; // CASE_ID (PK)
	private Long memberId; // MEMBER_ID (FK)
	private Long lawyerId; // LAWYER_ID (FK)
	private String title; // TITLE
	private String caseType; // CASE_TYPE
	private String description; // DESCRIPTION
	private String step; // STEP (RECEIVED / ASSIGNED / IN_PROGRESS / OPINION_READY / CLOSED)
	private String expertOpinion;// EXPERT_OPINION
	private Date createdAt; // CREATED_AT
	private Date updatedAt; // UPDATED_AT
	private Date closedAt; // CLOSED_AT

	// 💡 [프론트엔드 연동용 가상 필드] DB 테이블에는 없지만 화면에 띄우거나 데이터를 받을 때 필요한 필드들
	private String clientName; // 의뢰인 이름
	private String lawyerName; // 담당 변호사 이름
	private List<AttachmentVO> files; // 다중 파일 첨부 목록

	// 💡 [추가] 사건 페이지에서 후기를 표시하기 위한 필드
	private Integer reviewRating;
	private String reviewContent;
	private String reviewDate;
}