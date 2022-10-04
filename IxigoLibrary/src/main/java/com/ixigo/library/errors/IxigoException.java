package com.ixigo.library.errors;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown by the Ixigo projects
 * 
 * @author Marco
 *
 */
public class IxigoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final HttpStatus status;
	private final String message;

	public IxigoException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
