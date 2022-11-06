package com.ixigo.library.errors;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Exception thrown by the Ixigo projects
 * 
 * @author Marco
 *
 */
@JsonIgnoreProperties({ 
"cause",
"stackTrace",
"suppressed",
"localizedMessage",
})
public class IxigoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	@JsonProperty("http_status")
	private final HttpStatus status;
	@JsonProperty("error_msg")
	private final String message;
	@JsonProperty("error_code")
	private final String errorCode;

	public IxigoException(HttpStatus status, String message, String errorCode) {
		this.status = status;
		this.message = message;
		this.errorCode = errorCode;
	}

	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
