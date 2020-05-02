package com.asisge.apirest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.asisge.apirest.config.response.ApiError;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.response.ApiSuccess;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.service.IAuditManager;

@Component
public abstract class BaseController {

	public static final String RESULT_CREATED = Messages.getString("message.result.created");
	public static final String RESULT_UPDATED = Messages.getString("message.result.updated");
	public static final String ACTION_CREATE = Messages.getString("message.action.create");
	public static final String ACTION_UPDATE = Messages.getString("message.action.update");
	public static final String ACTION_DELETE = Messages.getString("message.action.delete");

	@Autowired
	public IAuditManager auditManager;

	/**
	 * Método usado cuando hay errores en el binding result, retorna un bad_request
	 * con los errores existentes
	 * 
	 * @param result
	 * @return
	 */
	public ResponseEntity<ApiResponse> validateDto(BindingResult result) {
		List<String> errores = result.getFieldErrors().stream().map(err -> {
			String field = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(err.getField()), ' ') + ": ";
			return field + err.getDefaultMessage();
		}).collect(Collectors.toList());
		ApiError error = buildFail(Messages.getString("message.error.constraint-violation"));
		error.setErrors(errores);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * método usado para retornar respuestas de tipo 404
	 * 
	 * @param codigo
	 * @return
	 */
	public ResponseEntity<ApiResponse> respondNotFound(@Nullable String codigo) {
		String message;
		if (codigo != null && !codigo.isEmpty()) {
			message = Messages.getString("message.not-found.record");
			message = String.format(message, codigo);
		} else {
			message = Messages.getString("message.not-found.list");
		}
		ApiError error = buildFail(message);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	/**
	 * 
	 * Métodos para gestión de respuestas. Se retorna tipo ApiResponse en los
	 * controladores
	 * 
	 */
	public ApiSuccess buildOk(Object body) {
		ApiSuccess result = new ApiSuccess();
		result.setMessage(Messages.getString("message.result.success"));
		result.setBody(body);
		return result;
	}

	public ApiSuccess buildMessage(String message) {
		ApiSuccess result = new ApiSuccess();
		result.setMessage(message);
		return result;
	}

	public ApiSuccess buildSuccess(String message, Object body) {
		ApiSuccess result = new ApiSuccess();
		result.setMessage(message);
		result.setBody(body);
		return result;
	}

	public ApiSuccess buildSuccess(String message, Object body, String... args) {
		ApiSuccess result = new ApiSuccess();
		result.setMessage(message);
		result.formatMessage(args);
		result.setBody(body);
		return result;
	}

	public ApiSuccess buildDeleted(String... args) {
		ApiSuccess result = new ApiSuccess();
		String genericMessage = Messages.getString("message.result.deleted");
		result.setMessage(genericMessage);
		result.formatMessage(args);
		return result;
	}

	public ApiError buildFail(String message) {
		ApiError error = new ApiError();
		String result = (message != null && !message.isEmpty()) ? message : Messages.getString("message.generic-error");
		error.setMessage(result);
		return error;
	}

	public static boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
	}

	public static String getCurrentEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
