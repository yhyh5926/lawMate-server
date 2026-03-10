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
     * 1. 후기 등록 및 통계 갱신
     */
    @Transactional
    public boolean writeReview(ReviewVO reviewVO) {
        // 리뷰 등록 (Mapper 내에서 STATUS='DONE' 및 본인 여부 검증 수행)
        int result = reviewMapper.insertReview(reviewVO);
        
        // 등록 성공 시 변호사 평점/후기수 갱신
        if (result > 0) {
            reviewMapper.updateLawyerStats(reviewVO.getLawyerId());
            return true;
        }
        return false;
    }

    /**
     * 💡 2. 후기 삭제 (논리 삭제) 및 통계 갱신
     * @param reviewId 삭제할 리뷰 ID
     * @param memberId 삭제 요청한 회원 ID (본인 확인용)
     * @param lawyerId 통계를 갱신할 변호사 ID
     */
    @Transactional
    public boolean removeReview(int reviewId, int memberId, int lawyerId) {
        // 1. 리뷰 상태를 'DELETED'로 변경
        int result = reviewMapper.updateReviewStatusToDelete(reviewId, memberId);
        
        // 2. 삭제 성공 시 변호사 평점/후기수 즉시 갱신
        if (result > 0) {
            reviewMapper.updateLawyerStats(lawyerId);
            return true;
        }
        return false;
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