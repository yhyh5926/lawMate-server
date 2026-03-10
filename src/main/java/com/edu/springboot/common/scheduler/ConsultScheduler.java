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
}
