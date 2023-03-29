package com.ixigo.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError {
	@JsonProperty("error_message")
	private String message;
	@JsonProperty("error_field")
	private String field;
	@JsonProperty("error_value")
	private Object attemptedValue;
	@JsonProperty("error_code")
	private String code;

	public ValidationError() {

	}

	public ValidationError(String message) {
		this(null, message, null, null);
	}

	public ValidationError(final String field, final String message, final String code, final Object attemptedValue) {
		this.field = field;
		this.message = message;
		this.code = code;
		this.attemptedValue = attemptedValue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getAttemptedValue() {
		return attemptedValue;
	}

	public void setAttemptedValue(Object attemptedValue) {
		this.attemptedValue = attemptedValue;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
