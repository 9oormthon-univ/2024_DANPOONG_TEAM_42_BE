package com.groom.swipo.global.error;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.groom.swipo.global.error.dto.ErrorResponse;
import com.groom.swipo.global.error.exception.AccessDeniedGroupException;
import com.groom.swipo.global.error.exception.AuthGroupException;
import com.groom.swipo.global.error.exception.ConflictGroupException;
import com.groom.swipo.global.error.exception.InvalidGroupException;
import com.groom.swipo.global.error.exception.NotFoundGroupException;
import com.groom.swipo.global.error.exception.TeapotGroupException;
import com.groom.swipo.global.error.exception.TimeoutGroupException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
	// 400, InvalidGroupException
	@ExceptionHandler({InvalidGroupException.class})
	public ResponseEntity<ErrorResponse> handleInvalidData(RuntimeException e) {
		return createErrorResponse(e, HttpStatus.BAD_REQUEST);
	}

	// 401, AuthGroupException
	@ExceptionHandler({AuthGroupException.class})
	public ResponseEntity<ErrorResponse> handleAuthDate(RuntimeException e) {
		return createErrorResponse(e, HttpStatus.UNAUTHORIZED);
	}

	// 403, AccessDeniedGroupException
	@ExceptionHandler({AccessDeniedGroupException.class})
	public ResponseEntity<ErrorResponse> handleAccessDeniedDate(RuntimeException e) {
		return createErrorResponse(e, HttpStatus.FORBIDDEN);
	}

	// 404, NotFoundGroupException
	@ExceptionHandler({NotFoundGroupException.class})
	public ResponseEntity<ErrorResponse> handleNotFoundDate(RuntimeException e) {
		return createErrorResponse(e, HttpStatus.NOT_FOUND);
	}

	// 408, REQUEST_TIMEOUT
	@ExceptionHandler({TimeoutGroupException.class})
	public ResponseEntity<ErrorResponse> requestTimeout(RuntimeException e) {
		return createErrorResponse(e, HttpStatus.REQUEST_TIMEOUT);
	}

	// 409, ConflictGroupException
	@ExceptionHandler({ConflictGroupException.class})
	public ResponseEntity<ErrorResponse> handleConflict(RuntimeException e) {
		return createErrorResponse(e, HttpStatus.CONFLICT);
	}

	// 418, TeapotGroupException
	@ExceptionHandler({TeapotGroupException.class})
	public ResponseEntity<ErrorResponse> handleTeapotGroupException(RuntimeException e) {
		return createErrorResponse(e, HttpStatus.I_AM_A_TEAPOT);
	}

	// 메서드 인자 문제 생겼을 때
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		final MethodArgumentNotValidException e) {
		FieldError fieldError = Objects.requireNonNull(e.getFieldError());
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
			String.format("%s. (%s)", fieldError.getDefaultMessage(), fieldError.getField()));
		log.warn("Validation error for field {}: {}", fieldError.getField(), fieldError.getDefaultMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// 공통 에러 응답 생성
	private ResponseEntity<ErrorResponse> createErrorResponse(RuntimeException e, HttpStatus status) {
		ErrorResponse errorResponse = new ErrorResponse(status.value(), e.getMessage());
		log.error("Error [{}]: {}", status.value(), e.getMessage());

		return new ResponseEntity<>(errorResponse, status);
	}
}
