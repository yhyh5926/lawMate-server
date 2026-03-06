package com.edu.springboot.domain.attachment;

import com.edu.springboot.domain.attachment.vo.AttachmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AttachmentMapper {
    // 첨부파일 DB 등록
    int insertAttachment(AttachmentVO attachmentVO);
    
    // 특정 게시글, 사건 등에 첨부된 파일 목록 조회
    List<AttachmentVO> findAttachmentsByRef(@Param("refType") String refType, @Param("refId") Long refId);
    
    // 특정 파일 정보 조회 (다운로드 시 사용)
    AttachmentVO findAttachmentById(Long attachId);

    // 💡 [추가] 프로필 사진 변경 시 기존 사진 데이터를 삭제하기 위한 메서드
    int deleteAttachmentsByRef(@Param("refType") String refType, @Param("refId") Long refId);
}