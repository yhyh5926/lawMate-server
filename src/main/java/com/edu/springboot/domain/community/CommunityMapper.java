// src/main/java/com/edu/springboot/domain/community/CommunityMapper.java
package com.edu.springboot.domain.community;

//import java.util.ArrayList; 안쓴ㄷ
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
// 은혁 추가
import org.apache.ibatis.annotations.Param;
import com.edu.springboot.domain.community.vo.CommentVo;
import com.edu.springboot.domain.community.vo.PostVo;

@Mapper
public interface CommunityMapper {
	
	// 게시물 리스트
	public List<PostVo> list();
	public PostVo detail(int postId);
	public List<CommentVo> commentList(int postId);

	// [은혁 추가] 관리자 게시글 상태 업데이트 (삭제 처리용)
	public int updatePostStatus(@Param("postId") int postId, @Param("status") String status);
}