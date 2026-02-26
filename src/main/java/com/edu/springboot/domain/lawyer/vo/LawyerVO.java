package com.edu.springboot.domain.lawyer.vo;

import lombok.Data;

@Data
public class LawyerVO {
    private Long lawyerId;       // PK
    private Long memberId;       // 회원번호(FK)
    private String licenseNo;    // 자격번호
    private String specialty;    // 전문분야 (민사, 형사 등)
    private String intro;        // 소개글
    private String career;       // 경력사항
    private String officeName;   // 사무소명
    private String officeAddr;   // 사무소주소
    private Integer consultFee;  // 상담료
    private Double avgRating;    // 평균별점
    private Integer reviewCnt;   // 리뷰수
    private String approveStatus;// 승인상태 (APPROVED, PENDING)
    private String approvedAt;   // 승인일
    private String approvedBy;   // 승인자
    private String createdAt;    // 등록일
    private String updatedAt;    // 수정일
}