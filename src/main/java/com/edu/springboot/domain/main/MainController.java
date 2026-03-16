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
		// 1. 객체 생성
		MainResponseDTO res = new MainResponseDTO();

		// 2. 최신 공지사항 3건
		var notices = mainMapper.selectLatestNotices(3);
		res.setTopNotices(notices);
		res.setNotices(notices); // 구버전 호환

		// 3. 최근 판례 5건
		res.setPrecedents(mainMapper.selectRecentPrecedents());

		// 4. 최근 등록 변호사 5건
		res.setLawyers(mainMapper.selectRecentLawyers());

		// 5. 최근 등록 설문(모의재판) 5건
		res.setPolls(mainMapper.selectRecentPolls());

		// 6.최근 법률 질문 5건 (QuestionMapper 로직 기반)
		res.setQuestions(mainMapper.selectRecentQuestions());

		// 7.최근 커뮤니티 게시글 5건
		var recentPosts = mainMapper.selectRecentPosts(5);
		res.setRecentPosts(recentPosts);

		// 8.  today / weekly
		int todayCaseCount = mainMapper.countTodayCases();
		int weeklyConsultCount = mainMapper.countWeeklyConsults();
		res.setTodayCaseCount(todayCaseCount);
		res.setWeeklyConsultCount(weeklyConsultCount);

		// 9. stats 객체 구성 (프론트 차트용)
		MainStatsDTO stats = new MainStatsDTO();
		stats.setTodayCount(todayCaseCount);
		stats.setWeeklyCount(weeklyConsultCount);

		// 최근 7일 사건 수 차트 로직 (기존 유지)
		final int days = 7;
		final int offsetDays = Math.max(days - 1, 0);
		List<MainSeriesPointDTO> raw = mainMapper.selectDailyCaseCounts(offsetDays);

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
			String key = d.toString();
			MainSeriesPointDTO p = new MainSeriesPointDTO();
			p.setDate(key);
			p.setCount(map.getOrDefault(key, 0));
			filled.add(p);
		}
		stats.setSeries(filled);

		res.setStats(stats);

		return res;
	}

}