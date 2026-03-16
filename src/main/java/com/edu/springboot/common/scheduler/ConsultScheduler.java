package com.edu.springboot.common.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.edu.springboot.domain.consult.ConsultMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConsultScheduler {
    private final ConsultMapper consultMapper;

    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    public void deleteOldCancelledConsults() {
        consultMapper.deleteOldCancelledConsults();
    }
    
    @Scheduled(fixedDelay = 600000) // 매 10분마다
    public void autoCompletePastConsults() {
        int count = consultMapper.updatePastConfirmedToDone();
        if (count > 0) {
            System.out.println("[ConsultScheduler] CONFIRMED → DONE 자동 전환: " + count + "건");
        }
    }
}
