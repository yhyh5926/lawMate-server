package com.edu.springboot.domain.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.springboot.domain.poll.vo.PollOptionVo;
import com.edu.springboot.domain.poll.vo.PollVo;
import com.edu.springboot.domain.poll.vo.VoteVo;

@RestController
@RequestMapping("/api")
public class PollController {

    @Autowired
    CommunityMapper dao;

    // 의견조사 목록
    @GetMapping("/polls")
    public List<PollVo> pollList() {
        return dao.pollList();
    }

    @GetMapping("/poll/{pollId}")
    public PollVo pollDetail(@PathVariable("pollId") int pollId){
        return dao.pollDetail(pollId);
    }

    @GetMapping("/poll/{pollId}/options")
    public List<PollOptionVo> optionList(@PathVariable("pollId") int pollId){
        return dao.optionList(pollId);
    }

    @PostMapping("/poll/vote")
    public ResponseEntity<?> vote(@RequestBody VoteVo vote){

        // 이미 투표했는지 확인
        int exists = dao.checkAlreadyVoted(vote.getPollId(), vote.getMemberId());

        if(exists > 0){
            return ResponseEntity
                    .badRequest()
                    .body("이미 투표하셨습니다.");
        }

        // 투표 저장
        dao.insertVote(vote);

        // 득표수 증가
        dao.increaseVoteCnt(vote.getPollId(), vote.getOptionId());

        return ResponseEntity.ok("투표 완료");
    }
    
    @GetMapping("/poll/{pollId}/check/{memberId}")
    public boolean checkVoted(@PathVariable("pollId") int pollId, @PathVariable("memberId") int memberId){

        int result = dao.checkAlreadyVoted(pollId, memberId);

        return result > 0;
    }
    
    
}