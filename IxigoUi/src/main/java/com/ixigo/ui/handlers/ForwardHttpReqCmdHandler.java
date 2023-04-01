package com.ixigo.ui.handlers;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.util.UriBuilder;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.ui.commands.ForwardHttpReqCmd;
import com.ixigo.ui.config.IxigoEndPoints;
import com.ixigo.ui.constants.ErrorCodes;
import com.ixigo.ui.controllers.Forward;

import reactor.core.publisher.Mono;

@Component
public class ForwardHttpReqCmdHandler implements WebCommandHandler<ForwardHttpReqCmd, Object> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ForwardHttpReqCmdHandler.class);
	@Autowired
	private IxigoMessageResource msgSource;
	@Autowired
	private IxigoWebClientUtils webClient;
	@Autowired
	private IxigoEndPoints endPoints;
	
	private final int size = 16 * 1024 * 1024 * 100;
	private final ExchangeStrategies strategies = ExchangeStrategies.builder().codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size)).build();
	
	@Override
	public Mono<ResponseEntity<Object>> handle(ForwardHttpReqCmd cmd) {
		_LOGGER.trace("Inside ForwardHttpReqCmdHandler.handle");
		
		try {
			// Update the URI to let it point to the proxy Service
			var stringUri = cmd.getRequestedUri().replace(cmd.getContextPath() + Forward.REQUEST_MAPPING, endPoints.getEndPoints().get("gateway").get("base-url"));
			_LOGGER.debug(cmd.getRequestedUri());
			_LOGGER.debug(cmd.getContextPath());
			_LOGGER.debug(stringUri);
			var tmpBody = Serializable.class.cast(cmd.getBody());
			var url = new URL(stringUri);
			
			Mono<ResponseEntity<Object>> resp = null;
			
			/*
			 * Dem files requires more http cache, hence I'll use
			 * a custom strategy to allow the request to manage
			 * multiple Megabytes of data
			 */
			if(stringUri.endsWith(".dem")) { 
				// @formatter:off
				resp = webClient.getWebBuilder()
						.exchangeStrategies(strategies)
						.build()
						.get()
						.uri(uriBuilder -> {
							UriBuilder ub = uriBuilder
									.scheme(url.getProtocol())
									.host(url.getHost())
									.port(url.getPort())
									.path(url.getPath());
							return ub.build();
						})
						.retrieve()
						.toEntity(Resource.class)
						.map(r -> {
							var demFile = r.getBody();
							var demFileObj = Object.class.cast(demFile);
							return new ResponseEntity<Object>(demFileObj, r.getStatusCode());
						});
				// @formatter:on
			}
			resp = webClient.performRequest(Object.class, cmd.getHttpMethod(), url, Optional.empty(), Optional.ofNullable(cmd.getQueryParams()), Optional.empty(), Optional.ofNullable(tmpBody));
			
			return resp.map(r -> {
				_LOGGER.debug(String.format("Forwarded http resp status: %s", r.getStatusCode().toString()));
				return r;
			});
		}catch (MalformedURLException ex) {
			_LOGGER.error(ex.getMessage());
			throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(ErrorCodes.MALFORMAT_URI), ErrorCodes.MALFORMAT_URI);
		}
	}
}
