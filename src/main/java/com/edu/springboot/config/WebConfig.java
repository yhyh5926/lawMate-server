/**
 * 파일위치: src/main/java/com/edu/springboot/config/WebConfig.java
 * 기능전체: React와의 CORS 설정 및 서버 로컬 파일(uploads)의 정적 리소스 접근 설정을 관리합니다.
 */
package com.edu.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 1. CORS 설정: React(5173포트)와의 통신 허용
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // 2. 정적 리소스 설정: 서버의 uploads 폴더를 웹 경로로 매핑
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 웹 브라우저에서 /uploads/** 주소로 요청이 오면
        // 프로젝트 루트 폴더의 uploads/ 디렉토리에서 파일을 찾습니다.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        registry.addResourceHandler("/uploads/chat/**")
        		.addResourceLocations("file:uploads/chat/");
    }
}