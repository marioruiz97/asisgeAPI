package com.asisge.apirest.config.response;

public class ApiSuccess extends ApiResponse {
	
	private Object body;
	
	public ApiSuccess() {
		super("success");
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	
}
