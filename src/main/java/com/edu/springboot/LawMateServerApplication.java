package com.edu.springboot;

import org.mybatis.spring.annotation.MapperScan; // 1. 이 줄 추가
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.edu.springboot.domain") // 2. 이 줄 추가 (domain 폴더 안의 모든 매퍼 검색)
public class LawMateServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LawMateServerApplication.class, args);
	}

}