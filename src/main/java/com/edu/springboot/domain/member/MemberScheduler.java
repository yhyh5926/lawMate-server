// src/main/java/com/edu/springboot/domain/member/MemberScheduler.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.vo.MemberVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class MemberScheduler {

    private final MemberMapper memberMapper;

    public MemberScheduler(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    // 매일 새벽 3시에 자동 실행
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void anonymizeWithdrawnMembers() {
        // 30일 지난 탈퇴 회원 목록 불러오기
        List<MemberVO> targetMembers = memberMapper.findWithdrawnMembersForAnonymization();

        for (MemberVO member : targetMembers) {
            String randomUuid = UUID.randomUUID().toString().substring(0, 8);
            member.setLoginId("del_" + randomUuid);
            member.setEmail("del_" + randomUuid + "@deleted.com");

            memberMapper.anonymizeMember(member);
        }

        System.out.println("[스케줄러] 30일 경과 탈퇴 회원 비식별화 처리 완료. 대상 수: " + targetMembers.size());
    }
}