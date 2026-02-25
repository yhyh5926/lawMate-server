package com.edu.springboot.domain.notification.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotifySettingVO {
    private Long memberId;
    private String inappYn; // Y / N
    private String emailYn; // Y / N
    private String smsYn; // Y / N
    private LocalDateTime updatedAt;
}