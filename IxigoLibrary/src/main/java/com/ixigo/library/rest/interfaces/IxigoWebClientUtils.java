package com.ixigo.library.rest.interfaces;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public interface IxigoWebClientUtils {
	
	public WebClient.Builder getWebBuilder();
	
	public <T> Mono<ResponseEntity<T>> performGetRequestNoExceptions(Class<T> responseBodyClass, URL url, Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters);
	public <T> Mono<ResponseEntity<T>> performGetRequest(Class<T> responseBodyClass, URL url, Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters);
	
	 /**
     * It perform the REST request using the provided input parameters, and it will
     * throw IxigoExceptions if an error occurs
     * 
     * @param <T>
     * @param responseBodyClass
     * @param method
     * @param url
     * @param headers
     * @param queryParameters
     * @param contentType
     * @param body
     * @return
     */
    public <T> Mono<ResponseEntity<T>> performRequest(Class<T> responseBodyClass, HttpMethod method, URL url, Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters, Optional<MediaType> contentType,
            Optional<? extends Serializable> body);
    /**
     * It will perform the REST request without throwing WeExceptions
     * 
     * @param <T>
     * @param responseBodyClass
     * @param method
     * @param url
     * @param headers
     * @param queryParameters
     * @param contentType
     * @param body
     * @return
     */
    public <T> Mono<ResponseEntity<T>> performRequestNoExceptions(Class<T> responseBodyClass, HttpMethod method, URL url, Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters, Optional<MediaType> contentType,
            Optional<? extends Serializable> body);
}
