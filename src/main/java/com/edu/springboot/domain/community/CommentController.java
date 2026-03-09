package com.edu.springboot.domain.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@PostMapping("/comments")
	public void writeComment(@RequestBody CommentVo commentVo) {
		System.out.println("=== 댓글 등록 시작 ===");
	    System.out.println("postId = " + commentVo.getPostId());
	    System.out.println("memberId = " + commentVo.getMemberId());
	    System.out.println("parentId = " + commentVo.getParentId());
	    System.out.println("content = " + commentVo.getContent());
		
		dao.insertComment(commentVo);

	    // 부모댓글일 때만 게시글 댓글수 증가
	    if (commentVo.getParentId() == null) {
	        dao.increaseCommentCnt(commentVo.getPostId());
	    }
	}
	
	@DeleteMapping("/comments/{commentId}")
	public void deleteComment(@PathVariable("commentId") int commentId) {
	    CommentVo commentVo = dao.getCommentById(commentId);

	    if (commentVo != null) {
	        dao.deleteComment(commentId);

	        // 부모댓글일 때만 게시글 댓글수 감소
	        if (commentVo.getParentId() == null) {
	            dao.decreaseCommentCnt(commentVo.getPostId());
	        }
	    }
	}
}
