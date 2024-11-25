package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

// 설정 클래스로 지정. 프로젝트가 실행될 때 해당 클래스가 먼저 실행됨  
@Configuration
public class S3Config {

	@Value("${s3.access-key}")
	private String accessKey;
	
	@Value("${s3.secret-key}")
	private String secretKey;
	
	@Value("${s3.region}")
	private String region;
	
	// 인증 정보를 담아서 S3 객체 생성
	@Bean
	public AmazonS3 amazonS3() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		
		AmazonS3 s3;
		
		// S3 객체 생성 및 인증정보/지역 설정
		s3 =  AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region)
				.build();
		
		return s3;
		
	}
	
}
