// IntelliJ
// 파일위치: src/main/java/com/edu/springboot/domain/review/ReviewService.java

package com.edu.springboot.domain.review;

import com.edu.springboot.domain.review.vo.ReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ReviewService {

	@Autowired
	private ReviewMapper reviewMapper;

	/**
	 * 1. 후기 등록 (통계 갱신 로직 제거됨)
	 */
	@Transactional
	public boolean writeReview(ReviewVO reviewVO) {
		// 리뷰 등록 (Mapper 내에서 STATUS='DONE' 및 본인 여부 검증 수행)
		int result = reviewMapper.insertReview(reviewVO);

		// 💡 [에러 원인 제거] DB에 해당 컬럼이 없으므로 업데이트를 시도하지 않고 바로 결과를 반환합니다.
		return result > 0;
	}

	/**
	 * 2. 후기 삭제 (논리 삭제) (통계 갱신 로직 제거됨)
	 * 
	 * @param reviewId 삭제할 리뷰 ID
	 * @param memberId 삭제 요청한 회원 ID (본인 확인용)
	 * @param lawyerId 통계를 갱신할 변호사 ID (더 이상 사용 안 함)
	 */
	@Transactional
	public boolean removeReview(int reviewId, int memberId, int lawyerId) {
		// 1. 리뷰 상태를 'DELETED'로 변경
		int result = reviewMapper.updateReviewStatusToDelete(reviewId, memberId);

		// 💡 [에러 원인 제거] 삭제 성공 시에도 변호사 테이블 억지 업데이트를 시도하지 않습니다.
		return result > 0;
	}

	/**
	 * 3. 특정 변호사의 활성화된 리뷰 목록 조회
	 */
	public List<ReviewVO> getLawyerReviews(int lawyerId) {
		return reviewMapper.selectReviewsByLawyer(lawyerId);
	}

	/**
	 * 4. 작성 가능한 상담 내역 조회
	 */
	public List<ReviewVO> getMyPendingReviews(int memberId) {
		return reviewMapper.selectPendingReviewConsults(memberId);
	}
}