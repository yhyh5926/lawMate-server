package com.edu.springboot.domain.main.dto;

import com.edu.springboot.domain.main.vo.MainNoticeVO;
import com.edu.springboot.domain.main.vo.MainRecentPostVO;
import lombok.Data;

import java.util.List;

@Data
public class MainResponseDTO {

    /** 프론트(MainPage.jsx) 기대 키: topNotices */
    private List<MainNoticeVO> topNotices;

    /** 프론트(MainPage.jsx) 기대 키: stats */
    private MainStatsDTO stats;

    /** 최근 커뮤니티 게시글 */
    private List<MainRecentPostVO> recentPosts;

    // ---- Backward compatibility (optional) ----
    /** (구버전 호환) */
    private List<MainNoticeVO> notices;
    /** (구버전 호환) */
    private Integer todayCaseCount;
    /** (구버전 호환) */
    private Integer weeklyConsultCount;
}
