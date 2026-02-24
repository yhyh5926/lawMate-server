package com.edu.springboot.domain.chat.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomVO {

    private Long          roomNo;
    private Long          memberNo1;
    private Long          memberNo2;
    private LocalDateTime createdAt;

    /* 목록 조회용 JOIN 결과 */
    private Long          targetMemberNo;
    private String        targetName;
    private String        targetRole;       // MEMBER | LAWYER
    private String        lastMessage;
    private LocalDateTime lastMessageAt;
    private int           unreadCount;
}
