package com.asisge.apirest.config.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ApiError extends ApiResponse {

	@Getter
	@Setter
	private List<String> errors;

	public ApiError() {
		super("error");
	}	

}
