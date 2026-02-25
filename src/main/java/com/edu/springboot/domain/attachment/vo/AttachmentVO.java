package com.edu.springboot.domain.attachment.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttachmentVO {
    private Long attachId;
    private String refType; // CASE / CHAT / CONSULT / POST
    private Long refId;
    private Long uploaderId;
    private String origName;
    private String savePath;
    private Long fileSize;
    private String mimeType;
    private LocalDateTime createdAt;
}