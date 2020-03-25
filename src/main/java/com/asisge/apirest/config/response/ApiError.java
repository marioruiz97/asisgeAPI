package com.asisge.apirest.config.response;

public class ApiError extends ApiResponse {

	private String error;
	private String detailMessage;

	public ApiError() {
		super("error");
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getDetailMessage() {
		return detailMessage;
	}

	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}

}
