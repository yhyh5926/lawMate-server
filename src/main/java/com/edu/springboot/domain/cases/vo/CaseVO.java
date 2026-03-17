// src/main/java/com/edu/springboot/domain/cases/vo/CaseVO.java
package com.edu.springboot.domain.cases.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;
import com.edu.springboot.domain.attachment.vo.AttachmentVO;

// 사건 관련 데이터를 담는 객체
@Data
public class CaseVO {
//	사건 고유 식별자 (기본키)
	private Long caseId;

//	의뢰인 고유 식별자 (외래키)
	private Long memberId;

//	담당 변호 고유 식별자 (외래키)
	private Long lawyerId;

//	사건 제목
	private String title;

//	사건 분류 (예: 민사, 형사 등)
	private String caseType;

//	사건 상세 내용
	private String description;

//	현재 사건 진행 단계 (접수, 배당, 진행중, 의견서준비, 완료 등)
	private String step;

//	변호사의 전문 의견 내역
	private String expertOpinion;

//	사건 접수일자
	private Date createdAt;

//	사건 수정일자
	private Date updatedAt;

//	사건 종료(완료)일자
	private Date closedAt;

//	프론트엔드에 데이터를 전달할 때 필요한 가상 필드 (DB 테이블에는 없음)
//	의뢰인 이름
	private String clientName;

//	담당 변호사 이름
	private String lawyerName;

//	다중 파일 첨부 목록을 담는 리스트
	private List<AttachmentVO> files;

//	사건 페이지에서 사용자 후기를 화면에 보여주기 위해 사용하는 가상 필드
//	별점 점수
	private Integer reviewRating;

//	후기 내용
	private String reviewContent;

//	후기 작성 일자
	private String reviewDate;
}