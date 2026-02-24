package com.edu.springboot.domain.chat;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.springboot.common.jwt.JwtUtil;
import com.edu.springboot.common.response.ApiResponse;
import com.edu.springboot.domain.chat.vo.ChatMsgVO;
import com.edu.springboot.domain.chat.vo.ChatRoomVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatMapper chatMapper;
    private final JwtUtil    jwtUtil;

    /** 채팅방 목록 */
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomVO>>> getRooms(
            @RequestHeader("Authorization") String bearer) {

        Long memberNo = getMemberNo(bearer);
        return ResponseEntity.ok(
                ApiResponse.success(chatMapper.selectRoomList(memberNo)));
    }

    /** 채팅방 생성 or 기존 방 반환 */
    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse<ChatRoomVO>> getOrCreate(
            @RequestHeader("Authorization") String bearer,
            @RequestBody Map<String, Long> body) {

        Long myNo     = getMemberNo(bearer);
        Long targetNo = body.get("targetMemberNo");

        ChatRoomVO room = chatMapper.findRoomByMembers(myNo, targetNo);
        if (room == null) {
            room = ChatRoomVO.builder()
                    .memberNo1(myNo)
                    .memberNo2(targetNo)
                    .build();
            chatMapper.insertRoom(room);
        }
        return ResponseEntity.ok(ApiResponse.success(room));
    }

    /** 메시지 목록 (페이징) */
    @GetMapping("/rooms/{roomNo}/messages")
    public ResponseEntity<ApiResponse<List<ChatMsgVO>>> getMessages(
            @PathVariable Long roomNo,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "30") int size) {

        int offset = page * size;
        return ResponseEntity.ok(
                ApiResponse.success(chatMapper.selectMsgList(roomNo, offset, size)));
    }

    /** 읽음 처리 */
    @PutMapping("/rooms/{roomNo}/read")
    public ResponseEntity<ApiResponse<Void>> markRead(
            @PathVariable Long roomNo,
            @RequestHeader("Authorization") String bearer) {

        Long memberNo = getMemberNo(bearer);
        chatMapper.markAllRead(roomNo, memberNo);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── 헬퍼 ────────────────────────────────────────────────
    private Long getMemberNo(String bearer) {
        return jwtUtil.getMemberNo(bearer.replace("Bearer ", ""));
    }
}
