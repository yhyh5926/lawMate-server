package com.edu.springboot.domain.notification.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationVO {
    private Long notifyId;
    private Long memberId;
    private String notifyType; // CASE_UPDATE / CONSULT / PAYMENT / REVIEW 등
    private String title;
    private String content;
    private String refType; // CASE / CONSULT 등
    private Long refId;
    private String channel; // INAPP / EMAIL / SMS
    private String readYn; // Y / N
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
}