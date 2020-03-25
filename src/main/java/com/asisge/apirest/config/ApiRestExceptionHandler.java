package com.asisge.apirest.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.asisge.apirest.config.response.ApiError;
import com.asisge.apirest.config.utils.Messages;

@ControllerAdvice
public class ApiRestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<Object> handleConstraints(ConstraintViolationException ex, WebRequest request) {
		ApiError errorResponse = new ApiError();
		List<String> res = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
		errorResponse.setMessage(Messages.getString("message.error.constraint-violation"));
		errorResponse.setErrors(res);
		return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { DataAccessException.class })
	protected ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
		ApiError error = new ApiError();
		error.setMessage(ex.getMessage());
		error.setErrors(Arrays.asList(ex.getCause().getMessage()));
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleIllegalException(RuntimeException ex, WebRequest request) {
		ApiError error = new ApiError();
		error.setMessage(ex.getMessage());
		error.setErrors(Arrays.asList(ex.getCause().getMessage()));
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { NumberFormatException.class, NullPointerException.class })
	protected ResponseEntity<Object> handleGeneralExceptions(RuntimeException ex, WebRequest request) {
		ApiError error = new ApiError();
		error.setMessage(Messages.getString("message.error.number-or-null"));
		error.setErrors(Arrays.asList(ex.getMessage()));
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

}
