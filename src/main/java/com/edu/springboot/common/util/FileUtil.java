// 파일 위치: src/main/java/com/edu/springboot/common/util/FileUtil.java
package com.edu.springboot.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtil {

	// 기본 저장 경로
	private final String BASE_DIR = System.getProperty("user.dir") + "/uploads/";

	// 💡 [수정] 폴더명(subDir)을 매개변수로 받아 동적으로 경로를 설정하도록 오버로딩
	public String saveFile(MultipartFile file, String subDir) throws IOException {
		if (file == null || file.isEmpty())
			return null;

		// 예: /uploads/member/ 또는 /uploads/lawyer/
		String targetDir = BASE_DIR + subDir + "/";
		File directory = new File(targetDir);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// 파일명 중복 방지를 위한 UUID 적용
		String originalFilename = file.getOriginalFilename();
		String saveFilename = UUID.randomUUID().toString() + "_" + originalFilename;
		String savePath = targetDir + saveFilename;

		File dest = new File(savePath);
		file.transferTo(dest);

		// DB에 저장될 웹 접근 경로 (예: /uploads/member/파일명.jpg)
		return "/uploads/" + subDir + "/" + saveFilename;
	}

	// 기존 코드와의 호환성을 위한 기본 메서드 (기본은 member 폴더로 지정)
	public String saveFile(MultipartFile file) throws IOException {
		return saveFile(file, "member");
	}
}