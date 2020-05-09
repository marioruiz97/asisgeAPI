package com.asisge.apirest.config;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class InvalidProcessException extends RuntimeException {

	@Getter	
	private final HttpStatus status;

	public InvalidProcessException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
	
	public InvalidProcessException(HttpStatus status) {
		super();
		this.status = status;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
