package com.edu.springboot.domain.community.vo;

import lombok.Data;

@Data
public class PostVo {
	private int postId;
	private int memberId;
	private String caseType;
	private String title;
	private String content;
	private int viewCnt;
	private int commentCnt;
	private String status;
	private String createdAt;
	private String updatedAt;
	private String name;
}
