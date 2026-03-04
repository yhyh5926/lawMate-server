// src/main/java/com/edu/springboot/domain/lawyer/vo/LawyerVO.java
package com.edu.springboot.domain.lawyer.vo;

import java.util.List;
import com.edu.springboot.domain.review.vo.ReviewVO;
import lombok.Data;

@Data
public class LawyerVO {
	// 1. TB_MEMBER 컬럼 (공통 계정 정보)
	private int memberId;
	private String name;
	private String email;
	private String phone;

	// 2. TB_LAWYER 컬럼 (변호사 상세 정보)
	private int lawyerId;
	private String licenseNo;
	private String specialty;
	private String intro;
	private String career;
	private String officeName;
	private String officeAddr;

	// 💡 [오류 수정] DB에 추가된 변호사 사무소 상세 주소 필드 선언
	private String officeDetailAddr;

	private long consultFee;
	private double avgRating;
	private int reviewCnt;

	// 💡 추가: 변호사 승인 상태 (PENDING, APPROVED, REJECTED)
	private String approveStatus;

	// 3. TB_ATTACHMENT 컬럼 (이미지/첨부파일 연결)
	private int attachId; // 첨부파일 PK
	private String savePath; // 실제 파일 저장 경로 (/uploads/...)
	private String origName; // 원본 파일명

	// 4. 1:N 관계 매핑 (MyBatis Collection용)
	private List<ReviewVO> reviews;
}