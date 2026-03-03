package com.edu.springboot.domain.main.dto;

import com.edu.springboot.domain.main.vo.MainNoticeVO;
import com.edu.springboot.domain.main.vo.MainRecentPostVO;
import lombok.Data;

import java.util.List;

@Data
public class MainResponseDTO {
    private List<MainNoticeVO> notices;
    private int todayCaseCount;
    private int weeklyConsultCount;
    private List<MainRecentPostVO> recentPosts;
}
