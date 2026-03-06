// src/main/java/com/edu/springboot/domain/lawyer/LawyerController.java
package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

@RestController
@RequestMapping("/api/lawyers")
@CrossOrigin(origins = "http://localhost:5173") // 리액트 포트 허용
public class LawyerController {

	@Autowired
	private LawyerService lawyerService; // 💡 Mapper가 아닌 Service를 주입받습니다.

	@GetMapping
	public List<LawyerVO> list() {
		return lawyerService.getAllLawyers();
	}

	@GetMapping("/{id}")
	public LawyerVO detail(@PathVariable("id") Long id) {
		return lawyerService.getLawyerById(id);
	}
	
	//'26.03.04 원석 추가
	@GetMapping("/by-member/{memberId}")
	public LawyerVO detailByMember(@PathVariable("memberId") Long memberId) {
	    return lawyerService.getLawyerByMemberId(memberId);
	}

	@PutMapping("/{id}")
	public String updateLawyer(@PathVariable("id") int lawyerId, @RequestBody LawyerVO lawyerVO) {
		lawyerVO.setLawyerId(lawyerId);
		int result = lawyerService.updateLawyerProfile(lawyerVO);
		return result > 0 ? "success" : "fail";
	}
}