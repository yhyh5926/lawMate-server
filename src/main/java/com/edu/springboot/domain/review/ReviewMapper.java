package com.edu.springboot.domain.review;

import com.edu.springboot.domain.review.vo.ReviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 추가
import java.util.List;

@Mapper
public interface ReviewMapper {
    // 1. 후기 등록
    int insertReview(ReviewVO reviewVO);
    
    // 2. 변호사 평점 및 후기 수 갱신
    int updateLawyerStats(@Param("lawyerId") int lawyerId);
    
    // 3. 특정 변호사의 리뷰 목록 조회
    List<ReviewVO> selectReviewsByLawyer(@Param("lawyerId") int lawyerId);
    
    // 4. 작성 가능한 상담 내역 조회 (마이페이지 유도용)
    List<ReviewVO> selectPendingReviewConsults(@Param("memberId") int memberId);

    // 💡 5. 후기 삭제 (상태값을 'DELETED'로 변경)
    // 파라미터가 여러 개일 때는 @Param을 붙여주는 것이 XML 매핑 시 안전합니다.
    int updateReviewStatusToDelete(@Param("reviewId") int reviewId, @Param("memberId") int memberId);
}