package com.edu.springboot.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtil {
    
    // WebConfig에 설정될 기본 업로드 경로
    private final String uploadDir = "/uploads/lawmate/";

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String saveFilename = uuid + "_" + originalFilename;
        String savePath = uploadDir + saveFilename;

        File dest = new File(savePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        
        file.transferTo(dest);
        return savePath;
    }
}