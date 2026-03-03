package com.edu.springboot.domain.review.vo;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class ReviewVO {
	// TB_REVIEW 기본 컬럼
	private int reviewId; // 리뷰 PK
	private int consultId; // 상담 FK
	private int memberId; // 작성자(의뢰인) FK
	private int lawyerId; // 대상 변호사 FK
	private int rating; // 별점 (1~5)
	private String content; // 리뷰 내용
	private String status; // 상태 (ACTIVE, DELETED 등)

	// 날짜 관련
	private Timestamp createdAt; // 작성일
	private Timestamp updatedAt; // 수정일

	// 조인(Join)을 통해 가져올 추가 데이터
	private String reviewerName; // 마스킹 처리된 작성자 이름 (예: 홍*동)
	private String consultSummary;
}