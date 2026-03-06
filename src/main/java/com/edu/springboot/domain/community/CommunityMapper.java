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

    // 일반 게시글 목록 조회
    List<PostVo> list();

    // 일반 게시글 상세 조회
    PostVo detail(int postId);

    // 댓글 목록 조회
    List<CommentVo> commentList(int postId);

    // [관리자용] 일반 게시글 상태 변경 (삭제 처리)
    int updatePostStatus(@Param("postId") int postId, @Param("status") String status);

    // 모의판결 게시글 목록 조회
    List<PollVo> pollList();

    // 모의판결 게시글 상세 조회
    PollVo pollDetail(int pollId);

    // 모의판결 선택지 목록 조회
    List<PollOptionVo> optionList(int pollId);

    // 모의판결 투표 여부 확인
    int checkAlreadyVoted(@Param("pollId") int pollId, @Param("memberId") int memberId);

    // 모의판결 투표 등록
    int insertVote(VoteVo voteVo);

    // 모의판결 투표수 증가
    int increaseVoteCnt(@Param("pollId") int pollId, @Param("optionId") int optionId);
    
}