package com.ixigo.library.mediators.web.interfaces;

import org.springframework.http.ResponseEntity;

/**
 * Defines a handler for a web request
 * 
 * @author Marco
 * 
 * @param <T> Request
 * @param <R> Result type
 *
 */
public interface WebCommandHandler<T, R> {
	/**
	 * Handles a request
	 * 
	 * @param request Request parameter
	 * @return Result type
	 */
	public ResponseEntity<R> handle(T request);
}