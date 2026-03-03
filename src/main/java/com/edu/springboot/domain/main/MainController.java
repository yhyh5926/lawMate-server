/**
 * 파일위치: src/main/java/com/edu/springboot/domain/main/MainController.java
 * 기능전체: 메인 화면(/main.do)에서 필요한 공지사항/통계/최근게시글 데이터를 한번에 반환합니다.
 */
package com.edu.springboot.domain.main;

import com.edu.springboot.domain.main.dto.MainResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainMapper mainMapper;

    /**
     * ✅ 프론트 메인에서 사용 (기존 경로 유지)
     * - 최신 공지사항 3건
     * - 오늘 접수 사건 수
     * - 주간 누적 상담 건수(최근 7일, 오늘 포함)
     * - 최근 게시글 5건
     */
    @GetMapping("/main.do")
    public MainResponseDTO main() {
        MainResponseDTO res = new MainResponseDTO();
        res.setNotices(mainMapper.selectLatestNotices(3));
        res.setTodayCaseCount(mainMapper.countTodayCases());
        res.setWeeklyConsultCount(mainMapper.countWeeklyConsults());
        res.setRecentPosts(mainMapper.selectRecentPosts(5));
        return res;
    }
}
