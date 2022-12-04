package com.ixigo.library.rest.implementations;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.util.UriBuilder;

import com.ixigo.library.dto.ValidationFailure;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.library.validators.ValidationException;

import reactor.core.publisher.Mono;

public class IxigoWebClientUtilsImpl implements IxigoWebClientUtils {

	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoWebClientUtilsImpl.class);
    private static final String VALUE_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

    private WebClient.Builder builder;

    public IxigoWebClientUtilsImpl(WebClient.Builder builder) {
        this.builder = builder;
    }
	
	@Override
	public <T> Mono<ResponseEntity<T>> performRequest(Class<T> responseBodyClass, HttpMethod method, URL url, Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters, Optional<MediaType> contentType,
	        Optional<? extends Serializable> body) {
		/*
         * Create the request and adds query parameters if provided
         */
        RequestBodySpec rbs = getRequestBodySpec(method, url, headers, queryParameters, contentType);

        /*
         * Perform the call
         */
        ResponseSpec rs = null;
        if (body.isPresent()) {
            rs = rbs.bodyValue(body.get()).retrieve();
        } else {
            rs = rbs.retrieve();
        }

        rs.onStatus(s -> s == HttpStatus.BAD_REQUEST, clientResp -> {
            return clientResp.bodyToMono(ValidationFailure.class).flatMap(er -> {
                return Mono.error(new ValidationException(er.getErrors()));
            });
        });

        rs.onStatus(s -> s == HttpStatus.SERVICE_UNAVAILABLE, clientResp -> {
            _LOGGER.error(String.format("Service unavailable for endpoint %s", url.toString()));
            String msg = String.format("Url: %s not available", url.toString());
            throw new IxigoException(HttpStatus.BAD_GATEWAY, msg, "IXIGO0000");
        });

        rs.onStatus(HttpStatus::is4xxClientError, clientResp -> {
            String msg = String.format("Url: %s replied with: %s", url.toString(), clientResp.statusCode().toString());
            _LOGGER.error(msg);
            throw new IxigoException(clientResp.statusCode(), msg, "IXIGO0000");
        });
        return rs.toEntity(responseBodyClass);
	}

	@Override
	public <T> Mono<ResponseEntity<T>> performRequestNoExceptions(Class<T> responseBodyClass, HttpMethod method, URL url, Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters, Optional<MediaType> contentType,
	        Optional<? extends Serializable> body) {
		/*
         * Create the request and adds query parameters if provided
         */
        RequestBodySpec rbs = getRequestBodySpec(method, url, headers, queryParameters, contentType);

        /*
         * Perform the call
         */
        if (body.isPresent()) {
            rbs.bodyValue(body.get());
        }
        return rbs.exchangeToMono(response -> {
            if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                return Mono.just(new ResponseEntity<T>(response.statusCode()));
            }

            Mono<T> monoResp = response.bodyToMono(responseBodyClass);
            return mapToEntity(response, monoResp);
        }).onErrorResume(error -> {
        	return Mono.error(new IxigoException(HttpStatus.BAD_GATEWAY, error.getMessage(), ""));
        });
	}
	// @formatter:off
    private RequestBodySpec getRequestBodySpec(
            HttpMethod method,
            URL url,
            Optional<Map<String, String>> headers,
            Optional<Map<String, String>> queryParameters,
            Optional<MediaType> contentType) {
    // @formatter:on
        /*
         * Create the request and adds query parameters if provided
         */
        RequestBodySpec rbs = this.builder.build().method(method).uri(uriBuilder -> {
            UriBuilder ub = uriBuilder.scheme(url.getProtocol()).host(url.getHost()).port(url.getPort()).path(url.getPath());
            if (queryParameters.isPresent()) {
                for (Map.Entry<String, String> entry : queryParameters.get().entrySet()) {
                    ub = ub.queryParam(entry.getKey(), entry.getValue());
                }
            }
            return ub.build();

        }).contentType(contentType.orElse(MediaType.APPLICATION_JSON));

        /*
         * Add HTTP headers if provided
         */
        if (headers.isPresent()) {
            for (Map.Entry<String, String> entry : headers.get().entrySet()) {
                rbs = rbs.header(entry.getKey(), entry.getValue());
            }
        }

        return rbs;
    }

    @SuppressWarnings("unchecked")
    private <T> Mono<ResponseEntity<T>> mapToEntity(ClientResponse response, Mono<T> bodyMono) {
        // @formatter:off
        return ((Mono<Object>) bodyMono).defaultIfEmpty(VALUE_NONE).map(body ->
                new ResponseEntity<>(
                        body != VALUE_NONE ? (T) body : null,
                        response.headers().asHttpHeaders(),
                        response.rawStatusCode()));
        // @formatter:on
    }

	@Override
	public <T> Mono<ResponseEntity<T>> performGetRequestNoExceptions(Class<T> responseBodyClass, URL url, Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters) {
		return performRequestNoExceptions(responseBodyClass, HttpMethod.GET, url, headers, queryParameters, Optional.of(MediaType.APPLICATION_JSON), Optional.empty());
	}

	@Override
	public Builder getWebBuilder() {
		return this.builder;
	}

	@Override
	public <T> Mono<ResponseEntity<T>> performGetRequest(Class<T> responseBodyClass, URL url, Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters) {
		return performRequest(responseBodyClass, HttpMethod.GET, url, headers, queryParameters, Optional.of(MediaType.APPLICATION_JSON), Optional.empty());
	}

	@Override
	public <T> Mono<ResponseEntity<T>> performPostRequest(Class<T> responseBodyClass, URL url, Optional<? extends Serializable> body) {
		return performRequest(responseBodyClass, HttpMethod.POST, url, Optional.empty(), Optional.empty(), Optional.of(MediaType.APPLICATION_JSON), body);
	}
}
