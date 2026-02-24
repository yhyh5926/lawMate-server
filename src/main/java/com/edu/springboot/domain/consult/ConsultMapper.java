package com.edu.springboot.domain.consult;

import com.edu.springboot.domain.consult.vo.ConsultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConsultMapper {

    /** 예약 생성 */
    int insertConsult(ConsultVO vo);

    /** 예약 단건 조회 */
    ConsultVO selectConsultByNo(@Param("consultNo") Long consultNo);

    /** 회원 예약 전체 목록 */
    List<ConsultVO> selectConsultListByMember(@Param("memberNo") Long memberNo);

    /** 회원 예약 목록 - 상태 필터 */
    List<ConsultVO> selectConsultListByMemberAndStatus(
            @Param("memberNo") Long memberNo,
            @Param("status")   String status);

    /** 변호사 예약 목록 */
    List<ConsultVO> selectConsultListByLawyer(@Param("lawyerNo") Long lawyerNo);

    /** 특정 날짜 예약된 시간 목록 (가용 시간 계산용) */
    List<String> selectBookedTimes(@Param("lawyerNo")    Long lawyerNo,
                                    @Param("consultDate") String consultDate);

    /** 상태 변경 */
    int updateStatus(@Param("consultNo") Long consultNo,
                     @Param("status")    String status);

    /** 결제 완료 처리 */
    int updatePaidYn(@Param("consultNo") Long consultNo);

    /** 후기 작성 완료 처리 */
    int updateReviewedYn(@Param("consultNo") Long consultNo);
}
