package com.edu.springboot.domain.notification;

import com.edu.springboot.domain.notification.vo.NotificationVO;
import com.edu.springboot.domain.notification.vo.NotifySettingVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface NotificationMapper {
    // 알림 목록 조회 (인앱)
    List<NotificationVO> findByMemberId(Long memberId);
    
    // 알림 생성 (관리자 승인, 결제 완료 등 이벤트 발생 시 호출)
    int insertNotification(NotificationVO notification);
    
    // 알림 읽음 처리
    int updateReadStatus(Long notifyId);
    
    // 회원 알림 수신 설정 조회 및 업데이트
    NotifySettingVO findSettingByMemberId(Long memberId);
    int updateNotifySetting(NotifySettingVO setting);
}