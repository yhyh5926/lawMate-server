package com.edu.springboot.domain.main.vo;

import lombok.Data;

@Data
public class MainRecentPostVO {
    private int postId;
    private String title;
    private String name;       // 작성자 이름
    private String createdAt;  // 프론트 표시용 (YYYY-MM-DD 등)
}
