package com.edu.springboot.domain.attachment;

import com.edu.springboot.common.jwt.JwtUtil;
import com.edu.springboot.common.response.ApiResponse;
import com.edu.springboot.domain.attachment.vo.AttachmentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentMapper attachmentMapper;
    private final JwtUtil jwtUtil;

    @Value("${file.upload-dir:uploads/chat}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, Object>>> upload(
            @RequestHeader("Authorization") String bearer,
            @RequestPart("file") MultipartFile file,
            @RequestParam("roomNo") Long roomNo) throws IOException {

        Long uploaderId = jwtUtil.getMemberNo(bearer.replace("Bearer ", ""));

        String projectRoot = System.getProperty("user.dir");
        File dir = new File(projectRoot, uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String origName = file.getOriginalFilename();
        String saveName = UUID.randomUUID() + "_" + origName;
        File destFile = new File(dir, saveName);
        file.transferTo(destFile);

        String savePath = "/uploads/chat/" + saveName;

        AttachmentVO vo = new AttachmentVO();
        vo.setRefType("CHAT");
        vo.setRefId(roomNo);  // ← roomNo를 refId로 사용
        vo.setUploaderId(uploaderId);
        vo.setOrigName(origName);
        vo.setSavePath(savePath);
        vo.setFileSize(file.getSize());
        vo.setMimeType(file.getContentType());
        attachmentMapper.insertAttachment(vo);

        String fileUrl = "http://localhost:8080" + savePath;

        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "fileUrl", fileUrl,
                "originalName", origName
        )));
    }
}