// src/main/java/com/edu/springboot/domain/lawyer/LawyerController.java
package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

// 변호사 관련 데이터를 처리하는 컨트롤러
@RestController
@RequestMapping("/api/lawyers")
@CrossOrigin(origins = "http://localhost:5173")
public class LawyerController {

	@Autowired
	private LawyerService lawyerService;

//	등록된 전체 변호사 목록을 가져오는 API
	@GetMapping
	public List<LawyerVO> list() {
		return lawyerService.getAllLawyers();
	}

//	변호사 고유 아이디를 통해 특정 변호사의 상세 정보를 가져오는 API
	@GetMapping("/{id}")
	public LawyerVO detail(@PathVariable("id") Long id) {
		return lawyerService.getLawyerById(id);
	}

//	일반 회원 아이디를 바탕으로 해당 회원이 등록한 변호사 정보를 가져오는 API
	@GetMapping("/by-member/{memberId}")
	public LawyerVO detailByMember(@PathVariable("memberId") Long memberId) {
		return lawyerService.getLawyerByMemberId(memberId);
	}

//	특정 변호사의 프로필 정보나 상세 이력을 수정하는 API
	@PutMapping("/{id}")
	public String updateLawyer(@PathVariable("id") int lawyerId, @RequestBody LawyerVO lawyerVO) {
		lawyerVO.setLawyerId(lawyerId);
		int result = lawyerService.updateLawyerProfile(lawyerVO);
		return result > 0 ? "success" : "fail";
	}

//	프론트엔드에서 전송한 변호사 프로필 사진을 받아 서버에 저장하는 API
	@PostMapping("/{id}/upload-profile")
	public String uploadProfileImage(@PathVariable("id") Long lawyerId, @RequestParam("file") MultipartFile file) {
		return lawyerService.updateProfileImage(lawyerId, file);
	}
}