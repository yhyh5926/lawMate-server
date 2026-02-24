package com.edu.springboot.domain.chat;

import com.edu.springboot.domain.chat.vo.ChatMsgVO;
import com.edu.springboot.domain.chat.vo.ChatRoomVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {

    // ── 채팅방 ──────────────────────────────────────────────

    /** 두 회원 간 기존 채팅방 조회 */
    ChatRoomVO findRoomByMembers(@Param("no1") Long no1, @Param("no2") Long no2);

    /** 채팅방 생성 */
    int insertRoom(ChatRoomVO vo);

    /** 내 채팅방 목록 (마지막 메시지·안읽음 수 포함) */
    List<ChatRoomVO> selectRoomList(@Param("memberNo") Long memberNo);

    /** 채팅방 단건 조회 */
    ChatRoomVO selectRoomByNo(@Param("roomNo") Long roomNo);

    // ── 메시지 ──────────────────────────────────────────────

    /** 메시지 저장 */
    int insertMsg(ChatMsgVO vo);

    /** 메시지 목록 (페이징) */
    List<ChatMsgVO> selectMsgList(@Param("roomNo") Long roomNo,
                                   @Param("offset") int offset,
                                   @Param("limit")  int limit);

    /** 안읽은 메시지 수 */
    int countUnread(@Param("roomNo")   Long roomNo,
                    @Param("memberNo") Long memberNo);

    /** 읽음 처리 */
    int markAllRead(@Param("roomNo")   Long roomNo,
                    @Param("memberNo") Long memberNo);
}
