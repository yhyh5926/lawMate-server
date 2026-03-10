package com.edu.springboot.domain.review;

import com.edu.springboot.domain.review.vo.ReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	/**
	 * 1. 특정 변호사의 리뷰 목록 조회
	 */
	@GetMapping("/lawyer/{lawyerId}")
	public ResponseEntity<List<ReviewVO>> getReviews(@PathVariable int lawyerId) {
		return ResponseEntity.ok(reviewService.getLawyerReviews(lawyerId));
	}

	/**
	 * 2. 리뷰 작성 가능한 상담 목록 조회 (마이페이지용)
	 */
	@GetMapping("/pending/{memberId}")
	public ResponseEntity<List<ReviewVO>> getPendingReviews(@PathVariable int memberId) {
		return ResponseEntity.ok(reviewService.getMyPendingReviews(memberId));
	}

	/**
	 * 3. 리뷰 등록
	 */
	@PostMapping("/write")
	public ResponseEntity<?> writeReview(@RequestBody ReviewVO reviewVO) {
		boolean isSuccess = reviewService.writeReview(reviewVO);
		if (isSuccess) {
			return ResponseEntity.ok("리뷰가 등록되었습니다.");
		}
		return ResponseEntity.badRequest().body("리뷰 등록에 실패했습니다. 상담 상태를 확인하세요.");
	}

	/**
	 * 💡 4. 리뷰 삭제 삭제 시 본인 확인(memberId)과 통계 갱신(lawyerId)이 필요하므로
	 * 
	 * @RequestParam이나 맵으로 필요한 정보를 받습니다.
	 */
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<?> deleteReview(@PathVariable int reviewId, @RequestParam int memberId,
			@RequestParam int lawyerId) {

		boolean isDeleted = reviewService.removeReview(reviewId, memberId, lawyerId);

		if (isDeleted) {
			return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
		}
		return ResponseEntity.status(403).body("삭제 권한이 없거나 해당 리뷰를 찾을 수 없습니다.");
	}
}