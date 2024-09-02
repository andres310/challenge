package com.ravn.challenge.exception;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravn.challenge.model.ExceptionResponse;

/**
 * @author andres
 */
public class AuthException implements AuthenticationEntryPoint {
	
	final ObjectMapper mapper;

	public AuthException(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		ExceptionResponse exception = new ExceptionResponse(new Date(), "Not Authorized, you must log in", "");
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		
		mapper.findAndRegisterModules();
		mapper.writeValue(response.getOutputStream(), exception);
	}
}
