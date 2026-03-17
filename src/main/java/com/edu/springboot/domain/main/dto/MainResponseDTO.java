package com.edu.springboot.domain.main.dto;

import com.edu.springboot.domain.main.vo.MainLawyerVO;
import com.edu.springboot.domain.main.vo.MainNoticeVO;
import com.edu.springboot.domain.main.vo.MainPollVO;
import com.edu.springboot.domain.main.vo.MainPrecedentVO;
import com.edu.springboot.domain.main.vo.MainQuestionVO;
import com.edu.springboot.domain.main.vo.MainRecentPostVO;
import lombok.Data;

import java.util.List;

@Data
public class MainResponseDTO {

	private List<MainNoticeVO> topNotices;
	private MainStatsDTO stats;
	private List<MainRecentPostVO> recentPosts;
	private List<MainPrecedentVO> precedents;
	private List<MainLawyerVO> lawyers;
	private List<MainPollVO> polls;
	private List<MainQuestionVO> questions;

	// ---- Backward compatibility ----
	private List<MainNoticeVO> notices;
	private Integer todayConsultCount;
	private Integer weeklyConsultCount;
}