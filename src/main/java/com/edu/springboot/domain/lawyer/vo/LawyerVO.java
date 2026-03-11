package com.edu.springboot.domain.lawyer.vo;

import com.edu.springboot.domain.review.vo.ReviewVO;
import lombok.Data;
import java.util.List;

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

	// 💡 [추가] 채택 관련 통계 컬럼
	private int adoptCnt; // 누적 채택 수
	private int answerCnt; // 전체 답변 수

	private String approveStatus;
	private String createdAt;

	// 3. TB_ATTACHMENT 컬럼 (이미지/첨부파일 연결)
	private int attachId;
	private String savePath;
	private String origName;

	private List<String> filePaths;

	// 4. 1:N 관계 매핑
	private List<ReviewVO> reviews;

	// 💡 [추가] 응답 시 채택률을 실시간으로 계산해서 넘겨주는 Getter
	// 프론트엔드에서 'lawyer.adoptRate'로 바로 접근 가능합니다.
	public double getAdoptRate() {
		if (this.answerCnt == 0)
			return 0.0;
		double rate = ((double) this.adoptCnt / this.answerCnt) * 100;
		return Math.round(rate * 10.0) / 10.0; // 소수점 첫째 자리까지 반올림
	}
}