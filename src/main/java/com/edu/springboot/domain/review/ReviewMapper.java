// IntelliJ
// 파일위치: src/main/java/com/edu/springboot/domain/review/ReviewMapper.java

package com.edu.springboot.domain.review;

import com.edu.springboot.domain.review.vo.ReviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ReviewMapper {
	// 1. 후기 등록
	int insertReview(ReviewVO reviewVO);

	// 💡 [에러 원인 제거] updateLawyerStats 메서드 제거 완료

	// 3. 특정 변호사의 리뷰 목록 조회
	List<ReviewVO> selectReviewsByLawyer(@Param("lawyerId") int lawyerId);

	// 4. 작성 가능한 상담 내역 조회 (마이페이지 유도용)
	List<ReviewVO> selectPendingReviewConsults(@Param("memberId") int memberId);

	// 5. 후기 삭제 (상태값을 'DELETED'로 변경)
	int updateReviewStatusToDelete(@Param("reviewId") int reviewId, @Param("memberId") int memberId);
}