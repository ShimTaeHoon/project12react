package com.example.demo.security;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * CORS(Cross-Origin Resource Sharing) 문제를 해결하기 위한 필터 클래스
 * CORS: 다른 도메인에서 리소스를 요청할 때 발생하는 보안 문제
 * 모든 도메인에서 오는 요청을 허용하도록 설정
 * */

// @Order: 필터의 우선순위를 가장 높게 설정. 다른 필터들보다 먼저 실행됨
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter extends OncePerRequestFilter {

	// FilterChain: 다른 필터들을 호출하기 위한 객체
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// CORS 관련 헤더를 응답 메세지에 추가
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // 특정 도메인에서만 요청 허용
        response.setHeader("Access-Control-Allow-Credentials", "true"); // 자격증명을 포함한 요청을 허용
        response.setHeader("Access-Control-Allow-Methods", "*"); // 모든 HTTP 메서드 허용
        response.setHeader("Access-Control-Max-Age", "3600"); // 1시간 동안 캐시
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization"); // Authorization 헤더 추가

        // HTTP 메서드가 OPTIONS일 경우 프리플라이트 요청에 200 OK 응답
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response); // 다른 요청은 그대로 처리
        }

	}
}