package com.edu.springboot.domain.poll.vo;

import java.util.List;

import lombok.Data;

@Data
public class PollVo {
	private int pollId;
	private Integer postId;
	private int memberId;
	private String title;
	private String description;
	private String disclaimer;
	private String endDate;
	private String status;
	private String createdAt;
	private String name;
	private List<String> options;
}
