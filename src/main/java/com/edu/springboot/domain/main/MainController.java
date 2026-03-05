package com.edu.springboot.domain.main;

import com.edu.springboot.domain.main.dto.MainResponseDTO;
import com.edu.springboot.domain.main.dto.MainSeriesPointDTO;
import com.edu.springboot.domain.main.dto.MainStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {

    private final MainMapper mainMapper;

    @GetMapping("/main")
    public MainResponseDTO main() {
        MainResponseDTO res = new MainResponseDTO();

        // 최신 공지사항 3건
        var notices = mainMapper.selectLatestNotices(3);
        res.setTopNotices(notices);
        // 구버전 호환
        res.setNotices(notices);

        // today / weekly
        int todayCaseCount = mainMapper.countTodayCases();
        int weeklyConsultCount = mainMapper.countWeeklyConsults();
        // 구버전 호환
        res.setTodayCaseCount(todayCaseCount);
        res.setWeeklyConsultCount(weeklyConsultCount);

        // 최근 커뮤니티 게시글 5건
        var recentPosts = mainMapper.selectRecentPosts(5);
        res.setRecentPosts(recentPosts);

        // ===== stats (프론트 기대 형태) =====
        MainStatsDTO stats = new MainStatsDTO();
        stats.setTodayCount(todayCaseCount);
        stats.setWeeklyCount(weeklyConsultCount);

        // 최근 7일 사건 수 (차트)
        final int days = 7;
        final int offsetDays = Math.max(days - 1, 0); // ✅ days-1을 Java에서 계산
        List<MainSeriesPointDTO> raw = mainMapper.selectDailyCaseCounts(offsetDays);

        // 날짜 누락(0건)을 채워서 차트가 자연스럽게 보이게 함
        Map<String, Integer> map = new HashMap<>();
        for (MainSeriesPointDTO p : raw) {
            if (p != null && p.getDate() != null) {
                map.put(p.getDate(), p.getCount());
            }
        }

        List<MainSeriesPointDTO> filled = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = offsetDays; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            String key = d.toString(); // YYYY-MM-DD
            MainSeriesPointDTO p = new MainSeriesPointDTO();
            p.setDate(key);
            p.setCount(map.getOrDefault(key, 0));
            filled.add(p);
        }
        stats.setSeries(filled);

        res.setStats(stats);
        return res;
    }

    /** 공지 상세: 프론트(mainApi.getNoticeDetail)와 매칭 */
    @GetMapping("/notices/{id}")
    public Object noticeDetail(@PathVariable("id") long id) {
        return mainMapper.selectNoticeDetail(id);
    }
}