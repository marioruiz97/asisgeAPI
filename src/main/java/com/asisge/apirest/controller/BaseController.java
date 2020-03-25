package com.asisge.apirest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.asisge.apirest.config.response.ApiError;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.response.ApiSuccess;
import com.asisge.apirest.config.utils.AuditManager;
import com.asisge.apirest.config.utils.Messages;

@Component
public abstract class BaseController {

	public static final String RESULT_SUCCESS = Messages.getString("message.result.success");
	public static final String RESULT_CREATED = Messages.getString("message.result.created");
	public static final String RESULT_UPDATED = Messages.getString("message.result.updated");
	public static final String ACTION_CREATE = Messages.getString("message.action.create");
	public static final String ACTION_UPDATE = Messages.getString("message.action.update");
	public static final String ACTION_DELETE = Messages.getString("message.action.delete");

	@Autowired
	public AuditManager auditManager;

	/**
	 * crear metodos para los manejos de errores (illegal, numberFormat, DataAccess,
	 * nullPointer)
	 */

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
		String result = (message != null && !message.isEmpty()) ? message
				: Messages.getString("message.result.generic-error");
		error.setMessage(result);
		return error;
	}
}
