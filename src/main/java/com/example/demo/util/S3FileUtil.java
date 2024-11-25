package com.example.demo.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;

@Component
public class S3FileUtil {

	@Autowired
	AmazonS3 amazonS3;
	
	// 내 버킷의 이름
	String bucketName = "bucketlist111";
	
	// 스토리지에 파일을 업로드하고 처리 결과를 반환
	// 매개변수: 사용자에게 전달받은 파일 스트림
	// 반환값: 성공(S3 URL 주소) 실패(Null)
	public String fileUpload(MultipartFile image) {
		
		// 파일이 없으면 여기서 종료
		if(image.isEmpty() == true) {
			return null;
		}

		// S3 스토리지에 파일을 업로드하고 URL 주소 반환
		String url = uploadImageToS3(image);
		
		System.out.println("s3주소: " + url);
		
		return url;
		
	}
	
	// 실제로 AWS S3에 파일을 업로드하는 함수
	private String uploadImageToS3(MultipartFile image) {
		
		// 파일 스트림에서 파일명 추출
		String origin = image.getOriginalFilename();
		
		// 확장자 추출
		// 예시: test.png => png 추출
		int index = origin.lastIndexOf("."); // 예: 4
		String extention = origin.substring(index + 1); // 예: png
		
		// s3에 업로드할 파일이름. 중복이되지 않도록 설정
		// 같은 이름의 파일이 있을때 중복을 방지하기 위해 UUID 추가
		String s3FileName = UUID.randomUUID()
								.toString()
								.substring(0, 10) + origin;
		
		// 파일에서 스트림 추출
		InputStream is;
		
		// URL 주소
		String url = "";
		
		try {
			is = image.getInputStream();
			
			// 파일을 바이트 배열로 변환
			byte[] bytes = IOUtils.toByteArray(is);
			
			// S3에 전송하기 위한 바이트 배열 스트림 생성.. 스트림 타입 변환을 위해
			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		
			// 등록요청을 보내기 위해 헤더 설정
			ObjectMetadata metadata = new ObjectMetadata();
			
			// 파일의 타입과 크기 설정
			// 예시: image/png 또는 image/jpg
			metadata.setContentType("image/" + extention);
			metadata.setContentLength(bytes.length);
			
			// S3 스토리지에 파일을 전송
			// Put은 등록이나 수정
			// 생성자 인자: 버킷이름, 파일이름, 바이트스트림, 메타데이터(헤더)
			PutObjectRequest request = new PutObjectRequest(bucketName, s3FileName, byteStream, metadata);
			
			// 등록 요청을 전송
			amazonS3.putObject(request);
			
			// 등록된 파일의 주소를 조회
			// 인자: 버킷이름, s3파일이름
			// 반환값: 파일의 URL 주소
			url = amazonS3.getUrl(bucketName, s3FileName)
								 .toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return url;
		
	}
	
}
