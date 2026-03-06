// src/main/java/com/edu/springboot/domain/lawyer/LawyerService.java
package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

@Service // 💡 스프링이 관리하는 서비스 객체임을 선언
public class LawyerService {

	@Autowired
	private LawyerMapper lawyerMapper;

	public List<LawyerVO> getAllLawyers() {
		return lawyerMapper.selectAllLawyers();
	}

	public LawyerVO getLawyerById(Long id) {
		return lawyerMapper.selectLawyerById(id);
	}
	
	//26.03.04 원석 추가
	public LawyerVO getLawyerByMemberId(Long memberId) {
	    return lawyerMapper.selectLawyerByMemberId(memberId);
	}

	public int updateLawyerProfile(LawyerVO lawyerVO) {
		return lawyerMapper.updateLawyerProfile(lawyerVO);
	}
}