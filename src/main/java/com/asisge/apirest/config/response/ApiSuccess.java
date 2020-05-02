package com.asisge.apirest.config.response;

import lombok.Getter;
import lombok.Setter;

public class ApiSuccess extends ApiResponse {

	@Getter
	@Setter
	private Object body;

	public ApiSuccess() {
		super("success");
	}

}
