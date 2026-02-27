package com.edu.springboot.domain.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.springboot.domain.community.vo.CommentVo;

@RestController
@RequestMapping("/api")
public class CommentController {
	
	@Autowired
	CommunityMapper dao;
	
	@GetMapping("/comment/list/{postId}")
	public List<CommentVo> comments(
			@PathVariable("postId") int postId){
		return dao.commentList(postId);
	}
}
