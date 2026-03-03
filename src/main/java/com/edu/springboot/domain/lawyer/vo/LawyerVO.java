// src/main/java/com/edu/springboot/domain/lawyer/vo/LawyerVO.java
package com.edu.springboot.domain.lawyer.vo;

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
    
    // 💡 에러 해결: 승인 상태를 저장할 필드 추가
    private String approveStatus; 

    // TB_ATTACH 컬럼 (이미지 연결)
    private int attachId;
    private String savePath;
    private String origName;
}