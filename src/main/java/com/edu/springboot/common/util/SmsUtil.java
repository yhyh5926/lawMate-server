package com.edu.springboot.common.util;

import org.springframework.stereotype.Component;

@Component
public class SmsUtil {
    
    /**
     * 회원가입 및 아이디/비밀번호 찾기 시 SMS 인증번호 발송 유틸리티
     * (실제 운영에서는 CoolSMS 등 외부 API를 연동합니다)
     */
    public boolean sendVerificationCode(String phone, String code) {
        System.out.println("[SMS 발송] 수신번호: " + phone + " | 인증번호: [" + code + "]");
        // TODO: 외부 통신 모듈 연동
        return true; 
    }

    /**
     * 결제 완료, 알림 등 일반 메시지 발송
     */
    public void sendInfoMessage(String phone, String message) {
        System.out.println("[SMS 알림] 수신번호: " + phone + " | 내용: " + message);
    }
}