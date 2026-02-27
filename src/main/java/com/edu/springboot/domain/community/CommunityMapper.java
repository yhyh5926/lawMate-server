package com.edu.springboot.domain.community;


import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.edu.springboot.domain.community.vo.CommentVo;
import com.edu.springboot.domain.community.vo.PostVo;

@Mapper
public interface CommunityMapper {
	
	// 게시물 리스트
	public List<PostVo> list();
	public PostVo detail(int postId);
	public List<CommentVo> commentList(int postId);
}
