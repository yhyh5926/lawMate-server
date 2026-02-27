package com.edu.springboot;

import java.util.List;
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
    "org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"
})
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

      
            // 1. 변호사 JOIN 데이터 상세 체크
            try {
                List<LawyerVO> lawyers = lMapper.selectAllLawyers();
                System.out.println("✅ [변호사 데이터] 조회 성공 (총 " + lawyers.size() + "건)");
                
                if (!lawyers.isEmpty()) {
                    System.out.println("\n--- 🔍 변호사 JOIN 상세 매핑 결과 (상위 3건) ---");
                    for (int i = 0; i < Math.min(lawyers.size(), 3); i++) {
                        LawyerVO l = lawyers.get(i);
                        System.out.printf("[%d] 변호사ID: %d\n", i + 1, l.getLawyerId());
                        System.out.printf("    성함(Name): %s\n", (l.getName() != null ? l.getName() : "❌ NULL (매퍼 Alias 확인 필요)"));
                        System.out.printf("    소속사   : %s\n", l.getOfficeName());
                        System.out.printf("    이미지   : %s\n", (l.getSavePath() != null ? l.getSavePath() : "⚠️ 사진 없음"));
                        System.out.println("-".repeat(45));
                    }
                } else {
                    System.out.println("⚠️ [알림] 승인된(APPROVED) 변호사 데이터가 없습니다.");
                }
            } catch (Exception e) {
                System.err.println("❌ [변호사 데이터] JOIN 조회 오류");
                e.printStackTrace();
            }

            System.out.println("=".repeat(60) + "\n");
        };
    }
}