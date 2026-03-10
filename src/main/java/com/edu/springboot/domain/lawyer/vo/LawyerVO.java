package com.edu.springboot.domain.lawyer.vo;

import com.edu.springboot.domain.review.vo.ReviewVO; // ReviewVO 위치 확인 필요
import lombok.Data;
import java.util.List; // 💡 List 사용을 위해 반드시 필요함

@Data
public class LawyerVO {
	// 1. TB_MEMBER 컬럼 (공통 계정 정보)
	private int memberId;
	private String name;
	private String email;
	private String phone;
	private String loginId;

	// 2. TB_LAWYER 컬럼 (변호사 상세 정보)
	private int lawyerId;
	private String licenseNo;
	private String specialty;
	private String intro;
	private String career;
	private String officeName;
	private String officeAddr;
	private String officeDetailAddr;
	private long consultFee;
	private double avgRating;
	private int reviewCnt;

	// 변호사 승인 상태 (PENDING, APPROVED, REJECTED)
	private String approveStatus;

	// 가입일(신청일) 필드
	private String createdAt;

	// 3. TB_ATTACHMENT 컬럼 (이미지/첨부파일 연결)
	private int attachId;
	private String savePath;
	private String origName;

	// 💡 관리자 승인 페이지에서 다중 증빙서류를 받기 위한 리스트
	private List<String> filePaths;

	// 4. 1:N 관계 매핑 (MyBatis Collection용)
	// 상세 페이지에서 한 번에 조인해서 가져올 경우 사용
	private List<ReviewVO> reviews;
}