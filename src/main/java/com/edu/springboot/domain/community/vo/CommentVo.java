package com.edu.springboot.domain.community.vo;

import lombok.Data;

@Data
public class CommentVo {
	private int commentId;
	private int postId;
	private int memberId;
	private Integer parentId; // 부모 댓글 null떄문에 int말고 다른것
	private String content;
	private String boardType;
	private String status;
	private String createdAt;
	private String updatedAt;
	private String name;
}
