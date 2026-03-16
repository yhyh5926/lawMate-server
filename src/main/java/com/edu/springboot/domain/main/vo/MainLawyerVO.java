package com.edu.springboot.domain.main.vo;

import lombok.Data;

@Data
public class MainLawyerVO {
	private Long lawyerId;
	private String name;
	private String specialty;
	private String officeName;
	private String savePath; // 프로필 사진
}