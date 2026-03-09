package com.edu.springboot.domain.community;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.springboot.domain.community.vo.PostVo;

@RestController
@RequestMapping("/api")
public class PostController {
	
	@Autowired
	CommunityMapper dao;
	
	@GetMapping("/posts")
	public List<PostVo> postList(
	        @RequestParam(value = "sortType", defaultValue = "latest") String sortType){
	    return dao.list(sortType);
	}
	
	@GetMapping("/detail/{postId}")
	public PostVo qnaDetail(@PathVariable("postId") int postId){
		dao.increaseViewCnt(postId);
		return dao.detail(postId);
	}
	
	@GetMapping("/detail/viewless/{postId}")
	public PostVo qnaDetailWithoutView(@PathVariable("postId") int postId) {
	    return dao.detail(postId);
	}
	
	@PostMapping("/write")
	public void writePost(@RequestBody PostVo postVo) {
	    dao.insertPost(postVo);
	}
	
	@PutMapping("/edit")
	public void updatePost(@RequestBody PostVo postVo) {
	    dao.updatePost(postVo);
	}
	
	@DeleteMapping("/posts/{postId}")
	public void deletePost(@PathVariable("postId") int postId) {
	    dao.updatePostStatus(postId, "DELETED");
	}
	
	// 좋아요
	@GetMapping("/posts/topLiked")
	public List<PostVo> topLikedPosts() {
	    return dao.topLikedPosts();
	}
	
	@PostMapping("/posts/{postId}/like")
	public void togglePostLike(@PathVariable("postId") int postId,
	                           @RequestBody Map<String, Integer> request) {

	    int memberId = request.get("memberId");

	    int count = dao.checkPostLike(postId, memberId);

	    if (count == 0) {
	        dao.insertPostLike(postId, memberId);
	        dao.increaseLikeCnt(postId);
	    } else {
	        dao.deletePostLike(postId, memberId);
	        dao.decreaseLikeCnt(postId);
	    }
	}
	
	@GetMapping("/posts/{postId}/like/{memberId}")
	public int getPostLikeStatus(@PathVariable("postId") int postId,
	                             @PathVariable("memberId") int memberId) {
	    return dao.checkPostLike(postId, memberId);
	    // 안눌렀음 0 눌렀음 1
	}
}
