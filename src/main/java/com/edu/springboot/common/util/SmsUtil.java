/**
 * 파일위치: src/main/java/com/edu/springboot/common/util/SmsUtil.java
 * 수정사항: 외부 라이브러리(CoolSMS, JSON) 임포트를 모두 제거했습니다.
 * 💡 이 코드를 쓰면 build.gradle을 수정하지 않아도 빨간 줄이 생기지 않습니다.
 */
package com.edu.springboot.common.util;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class SmsUtil {

    @PostConstruct
    public void init() {
        System.out.println("✅ [common/util] SMS 인증 모듈(Mock)이 준비되었습니다.");
    }

    public String createCertificationNumber() {
        Random random = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            numStr.append(random.nextInt(10));
        }
        return numStr.toString();
    }

    public void sendSms(String to, String certificationNumber) {
        // 실제 전송 대신 콘솔에 출력 (라이브러리 충돌 방지)
        System.out.println("======= [SMS 전송] =======");
        System.out.println("수신: " + to);
        System.out.println("번호: [" + certificationNumber + "]");
        System.out.println("==========================");
    }
}