package com.edu.springboot.domain.attachment;

import com.edu.springboot.domain.attachment.vo.AttachmentVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AttachmentMapper {
    // 첨부파일 DB 등록
    int insertAttachment(AttachmentVO attachmentVO);
    
    // 특정 게시글, 사건 등에 첨부된 파일 목록 조회
    List<AttachmentVO> findAttachmentsByRef(String refType, Long refId);
    
    // 특정 파일 정보 조회 (다운로드 시 사용)
    AttachmentVO findAttachmentById(Long attachId);
}