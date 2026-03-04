// src/main/java/com/edu/springboot/common/util/FileUtil.java
package com.edu.springboot.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtil {

    // 로컬 폴더에 저장할 기본 경로 (원하시는 경로로 수정 가능)
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일명 중복 방지를 위한 UUID 적용
        String originalFilename = file.getOriginalFilename();
        String saveFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        String savePath = UPLOAD_DIR + saveFilename;

        File dest = new File(savePath);
        file.transferTo(dest);

        return "/uploads/" + saveFilename; // DB에 저장될 웹 접근 경로
    }
}