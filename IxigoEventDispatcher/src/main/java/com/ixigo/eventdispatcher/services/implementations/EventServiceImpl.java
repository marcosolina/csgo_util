package com.ixigo.eventdispatcher.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.eventdispatcher.models.database.Event_listenersDto;
import com.ixigo.eventdispatcher.repositories.interfaces.RepoEntityEventListener;
import com.ixigo.eventdispatcher.services.interfaces.EventService;
import com.ixigo.eventdispatcher.services.interfaces.NotificationService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;

import reactor.core.publisher.Mono;

public class EventServiceImpl implements EventService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);
	private static final Map<String, EventType> previousEvent = new ConcurrentHashMap<>();
	private static final String NEW_LINE_CHAR = "\n";

	@Autowired
	private IxigoMessageResource msgSource;
	@Autowired
	private RepoEntityEventListener repo;
	@Autowired
	private NotificationService notiService;

	@Override
	public Mono<Void> newIncomingEventFromServer(EventType event, String clientIp) {
		_LOGGER.debug(String.format("Processing evenve: %s from IP: %s", event.name(), clientIp));

		if (clientIp == null) {
			if (previousEvent.keySet().size() == 1) {
				clientIp = previousEvent.keySet().iterator().next();
			} else {
				_LOGGER.error("The request IP is null");
				return Mono.just("dummy").then();
			}
		}

		Mono<Void> dispatch = Mono.just("dummy").then();
		if (previousEvent.get(clientIp) == EventType.WARMUP_START && event == EventType.ROUND_START) {
			dispatch = dispatchEvent(EventType.WARMUP_END);
		}

		previousEvent.put(clientIp, event);
		return dispatch.then(dispatchEvent(event));
	}

	@Override
	public Mono<Boolean> registerNewListener(String listenerUrl, EventType event) throws IxigoException {
		checkListenerKey(listenerUrl, event);

		Event_listenersDto listener = new Event_listenersDto();

		/*
		 * https://stackoverflow.com/questions/53595420/correct-way-of-throwing-
		 */
		// @formatter:off
		return repo.findListener(listenerUrl, event)
				.flatMap(d -> Mono.error(new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage("DISP00001"))))
				.switchIfEmpty(repo.registerNewListener(listener))
				.flatMap(bool -> {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s- URL: %s", NEW_LINE_CHAR, listener.getUrl_listener()));
					sb.append(String.format("%s- Event: %s", NEW_LINE_CHAR, listener.getEvent_type().name()));
					
					return notiService.sendEventServiceError("New event listerner registered", sb.toString());
				});
		// @formatter:on

	}

	@Override
	public Mono<Boolean> deleteListener(String listenerUrl, EventType event) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	private Mono<Void> dispatchEvent(EventType event) {
		return null;
	}

	private void checkListenerKey(String listenerUrl, EventType event) throws IxigoException {
		if (event == null) {
			throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage("DISP00002"));
		}

		try {
			new URL(listenerUrl);
		} catch (MalformedURLException e) {
			throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage("DISP00003"));
		}
	}
}
