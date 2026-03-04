package com.edu.springboot.domain.poll.vo;

import lombok.Data;

@Data
public class PollOptionVo {

    private int optionId;
    private int pollId;
    private String optionText;
    private int voteCnt;

}