package com.example.demo.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {
	
	// 파일 경로는 환경에 따라 바뀔 수 있으므로
	// 환경파일에 저장하고 클래스에서 불러다 써야함!
	// 예: 로컬 컴퓨터 - c:/uploadfile
	//     서버 컴퓨터 - d:/imagefile
	
	//이미지 파일을 저장할 경로
	@Value("${filepath}")
	String filepath;
//	String filepath ="C:\\uploadfile\\"; << 위와 동일
	// value는 properties에 경로작성하고 사용하는것
	
	// 매개변수: 파일 스트림
	// 전달받은 파일 스트림을 컴퓨터에 저장하고 파일이름을 반환
	public String fileUpload(MultipartFile multipartFile) {
		Path copyOfLocation = null;
		
		if(multipartFile == null || multipartFile.isEmpty()) { //파일스트림이 없으면 메소드를 종료
			return null;
		}
		try {
			
			copyOfLocation = Paths
					.get(filepath + File.separator + StringUtils.cleanPath(multipartFile.getOriginalFilename())); 
			
			//파일을 폴더에 저장
			Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//파일이름을 반환
		return multipartFile.getOriginalFilename();
	}
}