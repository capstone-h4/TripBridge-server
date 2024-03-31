package com.example.tripbridgeserver.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// AuthenticationEntryPoint => Spring Security 인터페이스
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.getWriter().write("{ \"code:\": \" \",\"message\": \"no permission\" }");
	}
}