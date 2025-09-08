package com.viniciusmcabral.sound_rate.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Map<String, Object>> handleNoSuchElementException(NoSuchElementException e,
			HttpServletRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now());
		body.put("status", HttpStatus.NOT_FOUND.value());
		body.put("error", "Not Found");
		body.put("message", e.getMessage());
		body.put("path", request.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException e,
			HttpServletRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now());
		body.put("status", HttpStatus.CONFLICT.value());
		body.put("error", "Conflict");
		body.put("message", e.getMessage());
		body.put("path", request.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpServletRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Validation Error");
		body.put("path", request.getRequestURI());

		Map<String, String> fieldErrors = new HashMap<>();
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			fieldErrors.put(f.getField(), f.getDefaultMessage());
		}
		body.put("errors", fieldErrors);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}