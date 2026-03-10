package com.edu.springboot.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtil {

<<<<<<< HEAD
	// 로컬 폴더에 저장할 기본 루트 경로
	private final String ROOT_DIR = System.getProperty("user.dir");
=======
    // 로컬 폴더에 저장할 기본 경로 (원하시는 경로로 수정 가능)
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/lawyer/";
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git

	/**
	 * 파일을 특정 도메인 폴더에 저장합니다.
	 * 
	 * @param file    업로드된 파일
	 * @param refType 저장할 폴더명 (예: "lawyer", "question")
	 * @return DB에 저장될 웹 접근 경로 (/uploads/폴더명/파일명)
	 */
	public String saveFile(MultipartFile file, String refType) throws IOException {
		if (file == null || file.isEmpty())
			return null;

		// 💡 1. 하위 폴더 경로 설정 (소문자로 통일하여 관리하기 편하게 함)
		String subDir = "/uploads/" + refType.toLowerCase() + "/";
		String absoluteDirPath = ROOT_DIR + subDir;

		// 💡 2. 디렉토리 생성 (없으면 만들기)
		File directory = new File(absoluteDirPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// 💡 3. 파일명 중복 방지를 위한 UUID 적용
		String originalFilename = file.getOriginalFilename();
		String saveFilename = UUID.randomUUID().toString() + "_" + originalFilename;

<<<<<<< HEAD
		// 💡 4. 물리적 파일 저장 위치
		String fullPath = absoluteDirPath + saveFilename;

		File dest = new File(fullPath);
		file.transferTo(dest);

		// 💡 5. DB에 저장될 상대 경로 리턴
		return subDir + saveFilename;
	}
=======
        return "/uploads/lawyer/" + saveFilename; // DB에 저장될 웹 접근 경로
    }
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git
}