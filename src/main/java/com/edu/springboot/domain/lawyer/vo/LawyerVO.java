// src/main/java/com/edu/springboot/domain/lawyer/vo/LawyerVO.java
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

<<<<<<< HEAD
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
=======
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
    
    // 💡 에러 해결: 승인 상태를 저장할 필드 추가
    private String approveStatus; 
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git

<<<<<<< HEAD
	// TB_ATTACH 컬럼 (이미지 연결)
	private int attachId; // 첨부파일 PK
	private String savePath; // 실제 파일 저장 경로 (/uploads/...)
	private String origName; // 원본 파일명

	// 1:N 관계를 위한 리뷰 리스트
	private List<ReviewVO> reviews;
=======
    // TB_ATTACH 컬럼 (이미지 연결)
    private int attachId;
    private String savePath;
    private String origName;
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git
}