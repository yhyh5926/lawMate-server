package com.edu.springboot.domain.lawyer.vo;

import java.util.List;

import com.edu.springboot.domain.review.vo.ReviewVO;

import lombok.Data;

@Data
public class LawyerVO {
	// TB_MEMBER 컬럼
	private int memberId;
	private String name;
	private String email;
	private String phone;

	// TB_LAWYER 컬럼
	private int lawyerId;
	private String licenseNo;
	private String specialty;
	private String intro;
	private String career;
	private String officeName;
	private String officeAddr;
	private long consultFee;
	private double avgRating;
	private int reviewCnt;

	// TB_ATTACH 컬럼 (이미지 연결)
	private int attachId; // 첨부파일 PK
	private String savePath; // 실제 파일 저장 경로 (/uploads/...)
	private String origName; // 원본 파일명

	// 1:N 관계를 위한 리뷰 리스트
	private List<ReviewVO> reviews;
}