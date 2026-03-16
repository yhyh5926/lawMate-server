package com.edu.springboot.domain.main.vo;

import lombok.Data;

@Data
public class MainPollVO {
	private Long pollId;
	private String title;
	private String endDate;
	private String status;
}