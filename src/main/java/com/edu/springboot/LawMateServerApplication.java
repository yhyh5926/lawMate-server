package com.edu.springboot;

import java.util.List;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//수정
// 시큐리티 자동 설정 제외를 위해 import 대신 excludeName 방식을 사용하여 import 에러 원천 차단
//원본
/* import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
 */

import org.springframework.context.annotation.Bean;

import com.edu.springboot.domain.precedent.PrecedentMapper;
import com.edu.springboot.domain.precedent.PrecedentVO;

// 1. 중복된 어노테이션을 하나로 합치고, 필요한 설정을 통합했습니다.
@MapperScan("com.edu.springboot.domain")
//수정
@SpringBootApplication(excludeName = {
    "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
    "org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"
})
//원본
/* @SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
 */
public class LawMateServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawMateServerApplication.class, args);
    }

    // 🚀 서버 기동 즉시 DB 조회를 테스트하는 코드
    @Bean
    public CommandLineRunner testConnection(PrecedentMapper mapper) {
        return args -> {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🔍 [DB Debugging] 판례 데이터 조회 테스트를 시작합니다.");
            try {
                List<PrecedentVO> list = mapper.getPrecedentList();

                if (list != null) {
                    System.out.println("✅ [성공] DB 연결 및 조회에 성공했습니다!");
                    System.out.println("📊 [결과] 현재 DB에 저장된 판례 수: " + list.size() + "건");

                    if (!list.isEmpty()) {
                        System.out.println("📋 [샘플] 첫 번째 데이터: " + list.get(0));
                    } else {
                        System.out.println("⚠️ [경고] 연결은 됐으나 데이터가 0건입니다.");
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ [실패] 데이터 조회 중 오류가 발생했습니다.");
                System.err.println("👉 에러 메시지: " + e.getMessage());
                e.printStackTrace(); // 에러 원인을 더 자세히 보기 위해 추가
            }
            System.out.println("=".repeat(50) + "\n");
        };
    }
}