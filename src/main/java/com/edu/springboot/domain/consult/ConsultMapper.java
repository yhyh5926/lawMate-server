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
    ConsultVO selectConsultByNo(@Param("consultId") Long consultId);

    /** 회원 예약 전체 목록 */
    List<ConsultVO> selectConsultListByMember(@Param("memberId") Long memberId);

    /** 회원 예약 목록 - 상태 필터 */
    List<ConsultVO> selectConsultListByMemberAndStatus(
            @Param("memberId") Long memberId,
            @Param("status")   String status);

    /** 변호사 예약 목록 */
    List<ConsultVO> selectConsultListByLawyer(@Param("lawyerId") Long lawyerId);

    /** 특정 변호사 예약된 날짜 목록 (가용 날짜 계산용) */
    List<String> selectBookedDates(@Param("lawyerId") Long lawyerId);

    /** 상태 변경 */
    int updateStatus(@Param("consultId") Long consultId,
                     @Param("status")    String status);
    
    void restoreConsult(Long consultId);
    void deleteConsult(Long consultId);
    void deleteOldCancelledConsults();
    
    void updateReject(@Param("consultId") Long consultId, @Param("rejectReason") String rejectReason);
    
    Long selectLawyerIdByMemberId(Long memberId);
}