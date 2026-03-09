package com.edu.springboot.domain.community;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.edu.springboot.domain.community.vo.PostVo;
import com.edu.springboot.domain.community.vo.CommentVo;
import com.edu.springboot.domain.poll.vo.PollVo;
import com.edu.springboot.domain.poll.vo.PollOptionVo;
import com.edu.springboot.domain.poll.vo.VoteVo;

@Mapper
public interface CommunityMapper {
	
	// 게시물 리스트
	public List<PostVo> list(@Param("sortType") String sortType);
	public PostVo detail(int postId);
	public List<CommentVo> commentList(int postId);
	public void increaseViewCnt(@Param("postId") int postId);
	public void insertPost(PostVo post);
	public void updatePost(PostVo post);

	// [은혁 추가] 관리자 게시글 상태 업데이트 (삭제 처리용)
	public int updatePostStatus(@Param("postId") int postId, @Param("status") String status);
	
	public List<PollVo> pollList();
    public PollVo pollDetail(int pollId);
    public List<PollOptionVo> optionList(int pollId);
    int checkAlreadyVoted(
    	    @Param("pollId") int pollId,
    	    @Param("memberId") int memberId
    	);
    public void insertVote(VoteVo vote);
    void increaseVoteCnt(
    	    @Param("pollId") int pollId,
    	    @Param("optionId") int optionId
    	);
}