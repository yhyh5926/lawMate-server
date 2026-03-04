package com.edu.springboot.domain.community;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.edu.springboot.domain.community.vo.CommentVo;
import com.edu.springboot.domain.community.vo.PostVo;
import com.edu.springboot.domain.poll.vo.PollOptionVo;
import com.edu.springboot.domain.poll.vo.PollVo;
import com.edu.springboot.domain.poll.vo.VoteVo;

@Mapper
public interface CommunityMapper {
	
	// 게시물 리스트
	public List<PostVo> list();
	public PostVo detail(int postId);
	public List<CommentVo> commentList(int postId);
	
	public List<PollVo> pollList();
    public PollVo pollDetail(int pollId);
    public List<PollOptionVo> optionList(int pollId);
    public int checkAlreadyVoted(int pollId, int memberId);
    public void insertVote(VoteVo vote);
    public void increaseVoteCnt(int pollId, int optionId);
}
