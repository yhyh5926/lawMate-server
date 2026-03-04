package com.edu.springboot.domain.poll.vo;

import lombok.Data;

@Data
public class PollVo {
	private int pollId;
	private int postId;
	private int memberId;
	private String title;
	private String description;
	private String disclaimer;
	private String endDate;
	private String status;
	private String createdAt;
	private String name;
}
