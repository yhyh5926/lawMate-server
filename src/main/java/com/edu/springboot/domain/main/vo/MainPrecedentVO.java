package com.edu.springboot.domain.main.vo;

import lombok.Data;

@Data
public class MainPrecedentVO {
	private Long precId;
	private String title;
	private String caseNo;
	private String court;
	private String judgeDate;
}