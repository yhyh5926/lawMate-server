package com.edu.springboot.domain.community.vo;

import lombok.Data;

@Data
public class PostVo {
	private int post_id;
	private int member_id;
	private String case_type;
	private String title;
	private String content;
	private int view_cnt;
	private int comment_cnt;
	private String status;
	private String created_at;
	private String updated_at;
	private String name;
}
