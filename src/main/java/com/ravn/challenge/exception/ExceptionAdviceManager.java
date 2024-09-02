package com.ravn.challenge.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ravn.challenge.model.ExceptionResponse;

/**
 * 
 * @author andres
 */
@ControllerAdvice
public class ExceptionAdviceManager {
	
	/**
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
		String message = "";
		for (var error : ex.getBindingResult().getAllErrors()) {
			message = message.concat(error.getDefaultMessage() + " ");
		}
		ExceptionResponse er = new ExceptionResponse(new Date(), message.trim(), request.getDescription(true));
		return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
	}
}
