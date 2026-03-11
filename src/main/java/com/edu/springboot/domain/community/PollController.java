package com.edu.springboot.domain.community;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Map<String, Object> pollList(
            @RequestParam(value = "sortType", defaultValue = "latest") String sortType,
            @RequestParam(value = "page", defaultValue = "1") int page) {
    	
    	dao.closeExpiredPolls();
    	
        int pageSize = 10;
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        List<PollVo> polls = dao.pollList(sortType, startRow, endRow);
        int totalCount = dao.getPollCount();

        Map<String, Object> result = new HashMap<>();
        result.put("polls", polls);
        result.put("totalCount", totalCount);
        result.put("currentPage", page);
        result.put("pageSize", pageSize);

        return result;
    }

    @GetMapping("/poll/{pollId}")
    public PollVo pollDetail(@PathVariable("pollId") int pollId){
    	dao.closeExpiredPolls();
    	return dao.pollDetail(pollId);
    }

    @GetMapping("/poll/{pollId}/options")
    public List<PollOptionVo> optionList(@PathVariable("pollId") int pollId){
        return dao.optionList(pollId);
    }

    @PostMapping("/poll/vote")
    public ResponseEntity<?> vote(@RequestBody VoteVo vote){
    	
    	dao.closeExpiredPolls();
    	
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
    
    @DeleteMapping("/polls/{pollId}")
    public void deletePoll(@PathVariable("pollId") int pollId) {
        dao.updatePollStatus(pollId, "DELETED");
    }
    
    @PostMapping("/polls/write")
    public ResponseEntity<?> writePoll(@RequestBody PollVo poll) {

        if (poll.getMemberId() == 0) {
            return ResponseEntity.badRequest().body("회원 정보가 없습니다.");
        }

        if (poll.getTitle() == null || poll.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("제목을 입력해주세요.");
        }

        if (poll.getOptions() == null || poll.getOptions().size() < 2) {
            return ResponseEntity.badRequest().body("선택지는 최소 2개 이상 필요합니다.");
        }
        
        if (poll.getEndDate() == null || poll.getEndDate().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("마감일을 선택해주세요.");
        }

        if (poll.getEndDate().compareTo(java.time.LocalDate.now().toString()) < 0) {
            return ResponseEntity.badRequest().body("과거 날짜는 마감일로 설정할 수 없습니다.");
        }

        dao.insertPollPost(poll);
        System.out.println("insertPollPost 후 postId = " + poll.getPostId());

        dao.insertPoll(poll);
        System.out.println("insertPoll 후 pollId = " + poll.getPollId());
        System.out.println("insertPoll 후 postId = " + poll.getPostId());

        for (String optionText : poll.getOptions()) {
            if (optionText != null && !optionText.trim().isEmpty()) {
                dao.insertPollOption(poll.getPollId(), optionText.trim());
            }
        }
        System.out.println("insertPollPost 후 postId = " + poll.getPostId());
        return ResponseEntity.ok("의견조사 등록 완료");
    }
    
    @PostMapping("/polls/edit")
    public ResponseEntity<?> editPoll(@RequestBody PollVo poll) {

        if (poll.getPollId() == 0) {
            return ResponseEntity.badRequest().body("pollId가 없습니다.");
        }

        if (poll.getTitle() == null || poll.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("제목을 입력해주세요.");
        }

        if (poll.getDescription() == null || poll.getDescription().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("설명을 입력해주세요.");
        }

        if (poll.getEndDate() == null || poll.getEndDate().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("마감일을 선택해주세요.");
        }

        dao.updatePoll(poll);

        return ResponseEntity.ok("의견조사 수정 완료");
    }
    
}