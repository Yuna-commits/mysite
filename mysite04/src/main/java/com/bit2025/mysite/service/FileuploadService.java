package com.bit2025.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileuploadService {
	
	// 업로드한 파일을 저장할 물리적 경로
	private static final String SAVE_PATH = "/mysite-uploads";
	// 웹에서 접근할 가상 경로
	private static final String URL = "/upload-images";
	/**
	 * 업로드한 파일(MultipartFile)을 서버에 저장
	 * -> 저장한 파일의 URL 반환
	 * @throws IOException 
	 */
	public String restore(MultipartFile multipartFile) throws RuntimeException {
		try {
			File uploadDirectory = new File(SAVE_PATH);

			// 1. 디렉토리가 없으면 생성, 생성 실패 시 저장 불가
			if (!uploadDirectory.exists() && !uploadDirectory.mkdirs()) {
				return null;
			}
			// 2. 업로드된 파일이 없으면 null 반환
			if (multipartFile.isEmpty()) {
				return null;
			}
			
			// 3. 원본 파일 정보 추출
			String originalFileName = multipartFile.getOriginalFilename();
			String extName = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
			
			// 4. 저장할 파일 이름 생성
			String saveFilename = generateSaveFileName(extName);
			
			// 5. 파일 저장
			byte[] data = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" +saveFilename);
			os.write(data);
			os.close();

			// 6. 클라이언트가 접근할 URL 반환
			return URL + "/" + saveFilename;
		} catch(IOException e) {
			throw new RuntimeException();
		}
	}

	private String generateSaveFileName(String extName) {
		Calendar calendar = Calendar.getInstance();
		
		return "" 
				+ calendar.get(Calendar.YEAR) 
				+ calendar.get(Calendar.MONTH) 
				+ calendar.get(Calendar.DATE)
				+ calendar.get(Calendar.HOUR) 
				+ calendar.get(Calendar.MINUTE) 
				+ calendar.get(Calendar.SECOND)
				+ calendar.get(Calendar.MILLISECOND) 
				+ ("." + extName);
	}
}
