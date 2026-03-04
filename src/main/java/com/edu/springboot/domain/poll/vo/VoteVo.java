package com.edu.springboot.domain.poll.vo;

import lombok.Data;

@Data
public class VoteVo {

    private int voteId;
    private int pollId;
    private int optionId;
    private int memberId;
    private String votedAt;

}