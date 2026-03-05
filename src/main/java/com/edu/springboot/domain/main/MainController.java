package com.edu.springboot.domain.main;

import com.edu.springboot.domain.main.dto.MainResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api") // 💡 프론트엔드 axios의 baseURL과 맞춤
public class MainController {

	private final MainMapper mainMapper;

	@GetMapping("/main")
	public MainResponseDTO main() {
		MainResponseDTO res = new MainResponseDTO();

		// 최신 공지사항 3건
		res.setNotices(mainMapper.selectLatestNotices(3));

		// 오늘 접수 사건 수 (Python 집계 데이터 기반 통계용)
		res.setTodayCaseCount(mainMapper.countTodayCases());

		// 주간 누적 상담 건수 (최근 7일)
		res.setWeeklyConsultCount(mainMapper.countWeeklyConsults());

		// 최근 커뮤니티 게시글 5건
		res.setRecentPosts(mainMapper.selectRecentPosts(5));

		return res;
	}
}