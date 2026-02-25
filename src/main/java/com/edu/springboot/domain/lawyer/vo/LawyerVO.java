package com.edu.springboot.domain.lawyer.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LawyerVO {
    private Long lawyerId;
    private Long memberId;
    private String licenseNo;
    private String specialty;
    private String intro;
    private String career;
    private String officeName;
    private String officeAddr;
    private BigDecimal consultFee;
    private Double avgRating;
    private Integer reviewCnt;
    private String approveStatus; // PENDING / APPROVED / REJECTED
}