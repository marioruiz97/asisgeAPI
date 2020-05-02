package com.asisge.apirest.config.response;

import lombok.Getter;
import lombok.Setter;

public class ApiResponse {

	@Getter
	private final String status;

	@Getter
	@Setter
	private String message;

	public ApiResponse(String status) {
		this.status = status;
	}

	public void formatMessage(String... args) {
		if (this.message != null && !this.message.isEmpty())
			this.message = String.format(this.message, args);
	}

}
