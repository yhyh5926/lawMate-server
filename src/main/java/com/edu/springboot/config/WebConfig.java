package com.edu.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//config 패키지에 WebConfig.java 생성 (선택 사항)
@Configuration
public class WebConfig implements WebMvcConfigurer {
 @Override
 public void addCorsMappings(CorsRegistry registry) {
     registry.addMapping("/**")
             .allowedOrigins("http://localhost:5173")
             .allowedMethods("GET", "POST", "PUT", "DELETE");
 }
}