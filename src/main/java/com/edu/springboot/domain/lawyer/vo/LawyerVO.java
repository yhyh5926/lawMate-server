// src/main/java/com/edu/springboot/domain/lawyer/vo/LawyerVO.java
package com.edu.springboot.domain.lawyer.vo;

import com.edu.springboot.domain.review.vo.ReviewVO;
import lombok.Data;
import java.util.List;

// 변호사 상세 정보와 관련된 데이터를 담는 객체
@Data
public class LawyerVO {
//	공통 회원 정보에 해당하는 고유 식별자
	private int memberId;
//  이름
	private String name;
//	이메일 주소
	private String email;
//	연락처
	private String phone;
//	로그인할 때 사용하는 아이디
	private String loginId;

	
//	변호사 전용 데이터 고유 식별자
	private int lawyerId;
//	자격증 및 면허 번호
	private String licenseNo;
//	전문 분야
	private String specialty;
//	한 줄 소개글
	private String intro;
//	경력 사항 내역
	private String career;
//	소속 법률 사무소 이름
	private String officeName;
//	사무소 기본 주소
	private String officeAddr;
//	사무소 상세 주소
	private String officeDetailAddr;
//	기본 상담 비용
	private long consultFee;

	
//	평균 별점
	private double avgRating;
//	작성된 리뷰 총 개수
	private int reviewCnt;
//	답변이 질문자에게 채택된 누적 횟수
	private int adoptCnt;
//	법률 상담 게시판에 작성한 전체 답변 수
	private int answerCnt;
//	관리자 승인 상태 여부
	private String approveStatus;
//	가입 또는 승인 일자
	private String createdAt;
//	프로필 이미지나 첨부파일 고유 식별자
	private int attachId;
//	서버에 저장된 파일 물리적 경로
	private String savePath;

	
//	사용자가 올린 원본 파일 이름
	private String origName;
//	다중 파일 경로를 담기 위한 리스트
	private List<String> filePaths;
//	변호사에게 달린 리뷰 목록을 매핑하기 위한 리스트
	private List<ReviewVO> reviews;
//	응답 시 채택률을 실시간으로 계산해서 넘겨주는 메서드
//	프론트엔드에서 adoptRate로 바로 접근해서 사용할 수 있다
	public double getAdoptRate() {
		if (this.answerCnt == 0)
			return 0.0;
		double rate = ((double) this.adoptCnt / this.answerCnt) * 100;
		return Math.round(rate * 10.0) / 10.0;
	}
}