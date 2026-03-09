// src/main/java/com/edu/springboot/domain/lawyer/LawyerController.java
package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

@RestController
@RequestMapping("/api/lawyers")
@CrossOrigin(origins = "http://localhost:5173")
public class LawyerController {

	@Autowired
	private LawyerService lawyerService;
	@GetMapping
	public List<LawyerVO> list() {
		return lawyerService.getAllLawyers();
	}

	@GetMapping("/{id}")
	public LawyerVO detail(@PathVariable("id") Long id) {
		return lawyerService.getLawyerById(id);
	}

	// '26.03.04 원석 추가
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

	// 프론트엔드에서 보낸 사진을 받는 API 연결
	@PostMapping("/{id}/upload-profile")
	public String uploadProfileImage(@PathVariable("id") Long lawyerId, @RequestParam("file") MultipartFile file) {
		return lawyerService.updateProfileImage(lawyerId, file);
	}
}