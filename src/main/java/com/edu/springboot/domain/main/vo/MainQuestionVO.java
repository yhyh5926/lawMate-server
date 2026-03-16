package com.edu.springboot.domain.main.vo;

import lombok.Data;

@Data
public class MainQuestionVO {
	private Long questionId;
	private String title;
	private String caseType;
	private String status;
	private String createdAt;
	private int answerCount; // 답변 개수 표시용
}