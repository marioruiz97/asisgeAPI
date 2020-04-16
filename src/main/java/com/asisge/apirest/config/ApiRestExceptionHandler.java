package com.asisge.apirest.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
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
		List<String> res = ex.getConstraintViolations().stream().map(err -> {
			String field = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(err.getPropertyPath().toString()),
					' ') + ": ";
			return field + err.getMessage();
		}).collect(Collectors.toList());
		errorResponse.setMessage(Messages.getString("message.error.constraint-violation"));
		errorResponse.setErrors(res);
		return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { DataIntegrityViolationException.class })
	protected ResponseEntity<Object> handleIntegrityExceptions(DataIntegrityViolationException ex, WebRequest request) {
		ApiError error = new ApiError();
		error.setMessage(Messages.getString("message.error.integrity-exception"));
		error.setErrors(Arrays.asList(ex.getLocalizedMessage(), ex.getMostSpecificCause().getLocalizedMessage()));
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(value = { DataAccessException.class })
	protected ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
		ApiError error = new ApiError();
		error.setMessage(ex.getLocalizedMessage());
		error.setErrors(Arrays.asList(ex.getMostSpecificCause().getLocalizedMessage()));
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleIllegalException(RuntimeException ex, WebRequest request) {
		ApiError error = new ApiError();
		error.setMessage(ex.getLocalizedMessage());
		error.setErrors(Arrays.asList(ex.getCause().getLocalizedMessage()));
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	// TODO verificar si se debe eliminar este handler
	@ExceptionHandler(value = {NullPointerException.class })
	protected ResponseEntity<Object> handleGeneralExceptions(RuntimeException ex, WebRequest request) {
		ApiError error = new ApiError();
		error.setMessage(Messages.getString("message.error.number-or-null"));
		error.setErrors(Arrays.asList(ex.getLocalizedMessage()));
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(value = {MailException.class})
	protected ResponseEntity<Object> handleMailExceptions(MailException exception, WebRequest request){
		ApiError error = new ApiError();
		error.setMessage(exception.getLocalizedMessage());
		error.setErrors(Arrays.asList(exception.getMostSpecificCause().getLocalizedMessage()));
		return handleExceptionInternal(exception, error, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
	}

}
