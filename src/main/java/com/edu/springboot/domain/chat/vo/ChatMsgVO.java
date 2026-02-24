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
public class ChatMsgVO {

    private Long          msgNo;
    private Long          roomNo;
    private Long          senderNo;
    private String        senderName;    // JOIN 결과

    /** TEXT | FILE | IMAGE */
    private String        msgType;

    private String        content;
    private String        fileUrl;

    /** Y: 읽음 / N: 안읽음 */
    private String        readYn;

    private LocalDateTime sentAt;
}
