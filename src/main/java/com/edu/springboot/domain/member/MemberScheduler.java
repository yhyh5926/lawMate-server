// src/main/java/com/edu/springboot/domain/member/MemberScheduler.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.vo.MemberVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// 정해진 시간에 반복 작업을 처리하는 스케줄러
@Component
public class MemberScheduler {

	private final MemberMapper memberMapper;

	public MemberScheduler(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

//	매일 새벽 3시마다 알아서 도는 기능
	@Scheduled(cron = "0 0 3 * * ?")
	@Transactional
	public void anonymizeWithdrawnMembers() {
//		탈퇴한 지 30일이 넘은 사람 목록을 DB에서 가져옴
		List<MemberVO> targetMembers = memberMapper.findWithdrawnMembersForAnonymization();

//		개인정보를 임의의 값으로 갈아치움
		for (MemberVO member : targetMembers) {
			String randomUuid = UUID.randomUUID().toString().substring(0, 8);
			member.setLoginId("del_" + randomUuid);
			member.setEmail("del_" + randomUuid + "@deleted.com");

			memberMapper.anonymizeMember(member);
		}

		System.out.println("[스케줄러] 30일 경과 탈퇴 회원 비식별화 처리 완료. 대상 수: " + targetMembers.size());
	}
}