package com.edu.springboot;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.edu.springboot.domain.precedent.PrecedentMapper;
import com.edu.springboot.domain.lawyer.LawyerMapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

@MapperScan("com.edu.springboot.domain")
@SpringBootApplication(excludeName = {
		"org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
		"org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration" })
public class LawMateServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LawMateServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner testConnection(PrecedentMapper pMapper, LawyerMapper lMapper) {
		return args -> {
			System.out.println("\n" + "=".repeat(60));
			System.out.println("🚀 [System Total Check] DB 연동 및 데이터 매핑 검증");
			System.out.println("=".repeat(60));

			try {
				long testLawyerId = 2L; // 콘솔에서 확인했던 그 lawyerId
				System.out.println("\n🔍 [리뷰 매핑 체크] 변호사 ID: " + testLawyerId + " 상세 조회 중...");

				LawyerVO detail = lMapper.selectLawyerById(testLawyerId);

				if (detail != null) {
					System.out.println("✅ [상세조회] 성공: " + detail.getName() + " 변호사");

					int reviewCnt = detail.getReviewCnt();

					if (reviewCnt > 0) {
						System.out.println("⭐⭐⭐⭐⭐ [리뷰 ] :" + reviewCnt + "건)");
					} else {
						System.out.println("⚠️ [알림] 리뷰 리스트가 비어있거나 null입니다.");
						System.out.println("   (DB의 TB_REVIEW에 STATUS='ACTIVE'인 데이터가 있는지 확인하세요.)");
					}
				} else {
					System.out.println("❌ [알림] 해당 ID(" + testLawyerId + ")의 변호사를 찾을 수 없습니다.");
				}
			} catch (Exception e) {
				System.err.println("❌ [리뷰 데이터] 조인 조회 중 예외 발생");
				e.printStackTrace();
			}

			System.out.println("=".repeat(60) + "\n");
		};
	}
}