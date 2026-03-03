package com.edu.springboot.domain.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.edu.springboot.common.jwt.JwtUtil;
import com.edu.springboot.domain.chat.vo.ChatMsgVO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMapper                   chatMapper;
    private final JwtUtil                      jwtUtil;

    @MessageMapping("/chat/message")
    public void handleMessage(
            @Payload ChatMessageRequest req,
            SimpMessageHeaderAccessor headerAccessor) {
        try {
            String bearer = headerAccessor.getFirstNativeHeader("Authorization");
            log.info("=== bearer: {}", bearer);

            Long senderNo = null;
            if (bearer != null) {
                String token = bearer.replace("Bearer ", "");
                senderNo = jwtUtil.getMemberNo(token);
            }
            log.info("=== senderNo: {}", senderNo);

            ChatMsgVO msg = ChatMsgVO.builder()
                    .roomNo(req.getRoomNo())
                    .senderNo(senderNo)
                    .msgType(req.getType() != null ? req.getType() : "TEXT")
                    .content(req.getContent())
                    .fileUrl(req.getFileUrl())
                    .readYn("N")
                    .build();

            chatMapper.insertMsg(msg);

            ChatMsgVO saved = chatMapper
                    .selectMsgList(req.getRoomNo(), 0, 1)
                    .stream()
                    .filter(m -> m.getMsgNo().equals(msg.getMsgNo()))
                    .findFirst()
                    .orElse(msg);

            messagingTemplate.convertAndSend(
                    "/sub/chat/room/" + req.getRoomNo(), saved);

        } catch (Exception e) {
            log.error("WebSocket 메시지 처리 오류: {}", e.getMessage());
        }
    }

    @Getter
    @Setter
    public static class ChatMessageRequest {
        private Long   roomNo;
        private String type;
        private String content;
        private String fileUrl;
    }
}