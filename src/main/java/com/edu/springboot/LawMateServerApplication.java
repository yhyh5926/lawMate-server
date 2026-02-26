package com.edu.springboot;

import java.util.List;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.edu.springboot.domain.precedent.PrecedentMapper;
import com.edu.springboot.domain.lawyer.LawyerMapper; // 변호사 매퍼 임포트 [cite: 2026-02-26]

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
            System.out.println("\n🚀 [System Check] DB 연동 상태 요약");

            // 1. 판례 데이터 한 줄 요약
            try {
                int pCount = pMapper.getPrecedentList().size();
                System.out.println("✅ [판례 데이터] 조회 성공 (총 " + pCount + "건)");
            } catch (Exception e) {
                System.err.println("❌ [판례 데이터] 조회 실패: " + e.getMessage());
            }

            // 2. 변호사 데이터 한 줄 요약
            try {
                int lCount = lMapper.selectAllLawyers().size();
                System.out.println("✅ [변호사 데이터] 조회 성공 (총 " + lCount + "건)");
            } catch (Exception e) {
                System.err.println("❌ [변호사 데이터] 조회 실패: " + e.getMessage());
            }

            System.out.println(); // 하단 여백
        };
    }
}