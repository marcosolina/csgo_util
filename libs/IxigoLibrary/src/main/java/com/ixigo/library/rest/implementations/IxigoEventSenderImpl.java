package com.ixigo.library.rest.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.eventdispatcher.models.rest.IncomingEventHttpRequest;
import com.ixigo.library.config.properties.EventDispatcherEndPoints;
import com.ixigo.library.rest.interfaces.IxigoEventSender;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import reactor.core.publisher.Mono;

public class IxigoEventSenderImpl implements IxigoEventSender {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoEventSenderImpl.class);
	@Autowired
	private EventDispatcherEndPoints eventDispatcherProps;
	@Autowired
	private IxigoWebClientUtils webClient;

	@Override
	public Mono<Boolean> sendIxigoEvent(EventType eventType) {
		IncomingEventHttpRequest event = new IncomingEventHttpRequest();
		event.setEventName(eventType.getDesc());
		try {
			URL url = new URL(eventDispatcherProps.getPostEvent());
			_LOGGER.debug(url.toString());

			return webClient.performPostRequest(Void.class, url, Optional.of(event))
					.map(r -> r.getStatusCode().is2xxSuccessful());

		} catch (MalformedURLException e) {
			_LOGGER.error(e.getMessage());
			e.printStackTrace();
			return Mono.just(false);
		}
	}

}
