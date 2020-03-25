package com.asisge.apirest.config.response;

import java.util.List;

public class ApiError extends ApiResponse {

	private List<String> errors;

	public ApiError() {
		super("error");
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
