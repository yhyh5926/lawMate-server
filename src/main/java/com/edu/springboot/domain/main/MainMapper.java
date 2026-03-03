package com.edu.springboot.domain.main;

import com.edu.springboot.domain.main.vo.MainNoticeVO;
import com.edu.springboot.domain.main.vo.MainRecentPostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MainMapper {

    /** 최신 공지사항 N건 */
    List<MainNoticeVO> selectLatestNotices(@Param("limit") int limit);

    /** 오늘 접수 사건 수 */
    int countTodayCases();

    /** 주간 누적 상담 건수 (최근 7일, 오늘 포함) */
    int countWeeklyConsults();

    /** 최근 게시글 N건 */
    List<MainRecentPostVO> selectRecentPosts(@Param("limit") int limit);
}
