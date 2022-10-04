package com.ixigo.library.mediators.web.interfaces;

import org.springframework.http.ResponseEntity;

/**
 * Defines Mediator to encapsulate the web layer requests/responses.
 * 
 * @author Marco
 *
 */
public interface WebMediator {
    /**
     * Send a request to a single handler.
     * 
     * @param request the actual command/query
     * @param <T>     the expected return type
     * @return Response object
     */
    public <T> ResponseEntity<T> send(WebCommandRequest<T> request);
}
