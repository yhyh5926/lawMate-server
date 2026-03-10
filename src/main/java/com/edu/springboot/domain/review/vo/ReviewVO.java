package com.edu.springboot.domain.review.vo;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class ReviewVO {
	private int reviewId;
	private int consultId;
	private int memberId;
	private int lawyerId;
	private int rating;
	private String content;
	private String status;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	// --- Join을 통해 가져올 추가 데이터 ---
	private String reviewerName; // 작성자 이름 (마스킹 처리용)
	private String lawyerName; // 변호사 이름 (리뷰 목록 확인용)
	private String officeName; // 사무소 이름
	private String consultDate; // 실제 상담이 진행됐던 날짜

}