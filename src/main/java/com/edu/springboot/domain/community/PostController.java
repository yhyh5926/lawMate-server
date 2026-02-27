package com.edu.springboot.domain.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.springboot.domain.community.vo.PostVo;

@RestController
@RequestMapping("/api")
public class PostController {
	
	@Autowired
	CommunityMapper dao;
	
	@GetMapping("/posts")
	public List<PostVo> postList(){
		return dao.list();
	}
	
	@GetMapping("/detail/{postId}")
	public PostVo qnaDetail(@PathVariable("postId") int postId){
		return dao.detail(postId);
	}
}
