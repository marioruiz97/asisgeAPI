package com.asisge.apirest.config.response;

public class ApiResponse {

	private final String status;
	private String message;

	public ApiResponse(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void formatMessage(String... args) {
		if (this.message != null && !this.message.isEmpty())
			this.message = String.format(this.message, args);
	}

}
