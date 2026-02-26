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
}