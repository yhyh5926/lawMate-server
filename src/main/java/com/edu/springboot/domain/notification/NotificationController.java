package com.edu.springboot.domain.notification;

import com.edu.springboot.domain.notification.vo.NotificationVO;
import com.edu.springboot.domain.notification.vo.NotifySettingVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationMapper notificationMapper;

    // 인앱 알림 목록 조회
    @GetMapping("/notification/list")
    public ResponseEntity<?> getNotificationList(@RequestParam Long memberId) {
        return ResponseEntity.ok(Map.of("data", notificationMapper.findByMemberId(memberId)));
    }

    // 알림 읽음 처리
    @PostMapping("/notification/read")
    public ResponseEntity<?> readNotification(@RequestParam Long notifyId) {
        notificationMapper.updateReadStatus(notifyId);
        return ResponseEntity.ok(Map.of("message", "읽음 처리 완료"));
    }

    // 알림 설정 수정 (마이페이지 연동)
    @PostMapping("/mypage/notify-setting")
    public ResponseEntity<?> updateSetting(@RequestBody NotifySettingVO settingVO) {
        notificationMapper.updateNotifySetting(settingVO);
        return ResponseEntity.ok(Map.of("message", "알림 설정이 변경되었습니다."));
    }
}