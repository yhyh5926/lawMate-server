package com.edu.springboot.domain.community.vo;

import lombok.Data;

@Data
public class CommentVo {
	private int commentId;
	private int postId;
	private int memberId;
	private int parentId;
	private String content;
	private String status;
	private String createdAt;
	private String updatedAt;
	private String name;
}
