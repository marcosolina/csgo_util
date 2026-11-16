package com.ixigo.library.mediators.web.interfaces;

import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;

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
	 * @param command Request parameter
	 * @return Result type
	 */
	public Mono<ResponseEntity<R>> handle(T cmd);
}
