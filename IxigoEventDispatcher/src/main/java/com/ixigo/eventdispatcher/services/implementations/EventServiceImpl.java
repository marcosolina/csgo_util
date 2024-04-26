package com.ixigo.eventdispatcher.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ixigo.eventdispatcher.constants.ErrorCodes;
import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.eventdispatcher.models.database.Event_listenersDto;
import com.ixigo.eventdispatcher.models.rest.DispatchedEventMessage;
import com.ixigo.eventdispatcher.repositories.interfaces.RepoEntityEventListener;
import com.ixigo.eventdispatcher.services.interfaces.EventService;
import com.ixigo.eventdispatcher.services.interfaces.NotificationService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.library.utils.DateUtils;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

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
	@Autowired
	private IxigoWebClientUtils webClient;

	@Override
	public Mono<Void> newIncomingEventFromServer(EventType event, String clientIp) {
		_LOGGER.debug(String.format("Processing event: %s from IP: %s", event.name(), clientIp));

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

		switch (event) {
		case DEM_FILES_PROCESSED:
			break;
		default:
			previousEvent.put(clientIp, event);
			break;
		}

		return dispatch.then(dispatchEvent(event));
	}

	@Override
	public Mono<Boolean> registerNewListener(String listenerUrl, EventType event) throws IxigoException {
		checkListenerKey(listenerUrl, event);

		Event_listenersDto listener = new Event_listenersDto();
		listener.setActive("Y");
		listener.setEvent_type(event);
		listener.setUrl_listener(listenerUrl);
		listener.setConsecutive_failure(0);

		/*
		 * https://stackoverflow.com/questions/53595420/correct-way-of-throwing-
		 * exceptions-with-reactor
		 */
		// @formatter:off
		return repo.findListener(listenerUrl, event)
				.flatMap(d -> Mono.error(new IxigoException(HttpStatus.CONFLICT, msgSource.getMessage(ErrorCodes.DUPLICATE_VALUE), ErrorCodes.DUPLICATE_VALUE)))
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

		return repo.deleteListener(listenerUrl, event).flatMap(bool -> {
			if (bool) {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%s- URL: %s", NEW_LINE_CHAR, listenerUrl));
				sb.append(String.format("%s- Event: %s", NEW_LINE_CHAR, event.name()));

				return notiService.sendEventServiceError("Deleted event listener", sb.toString());
			}
			return Mono.just(bool);
		});
	}

	private Mono<Void> dispatchEvent(EventType event) {
		DispatchedEventMessage message = new DispatchedEventMessage();
		message.setEventTime(DateUtils.getCurrentUtcDateTime());
		message.setEventType(event);
		// @formatter:off
		return repo.getListernersOfEvent(event)
		.parallel()
		.runOn(Schedulers.boundedElastic())
		.flatMap(dto -> {
			Mono<ResponseEntity<String>> mono = null;
			
			try {
				URL url = new URL(dto.getUrl_listener());
				mono = webClient.performRequestNoExceptions(String.class, HttpMethod.POST, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(message))
						.onErrorResume(er -> Mono.just(new ResponseEntity<String>(er.getMessage(), HttpStatus.BAD_GATEWAY)));
			} catch (Exception e) {
				e.printStackTrace();
				mono = Mono.just(new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
			}
			
			return mono.map(resp -> {
				return Tuples.of(dto, resp);
			});
		}).flatMap(tuple -> {
			var dto = tuple.getT1();
			var resp = tuple.getT2();
			// if successful rest otherwise set error
			if(resp.getStatusCode().is2xxSuccessful()) {
				dto.setLast_successful(DateUtils.getCurrentUtcDateTime());
				dto.setConsecutive_failure(0);
			}else {
				dto.setLast_failure(DateUtils.getCurrentUtcDateTime());
				dto.setConsecutive_failure(dto.getConsecutive_failure() + 1);
				if(dto.getConsecutive_failure() > 2) {
					dto.setActive("N");
				}
			}
			
			return repo.updateEntity(dto)
			.map(bool -> {
				if(!bool) {
					_LOGGER.error(String.format("Not able to update: %s", dto.toString()));
				}
				return Tuples.of(resp, dto);
			});
		}).filter(tuple -> !tuple.getT1().getStatusCode().is2xxSuccessful())
		.flatMap(tuple -> {
			var resp = tuple.getT1();
			var dto = tuple.getT2();
			StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s- URL: %s", NEW_LINE_CHAR, dto.getUrl_listener()));
            sb.append(String.format("%s- Event: %s", NEW_LINE_CHAR, dto.getEvent_type().name()));
            sb.append(String.format("%s- Attempt: %d", NEW_LINE_CHAR, dto.getConsecutive_failure()));
            sb.append(String.format("%s- Reason: %s", NEW_LINE_CHAR, resp.getBody()));
            
            return notiService.sendEventServiceError("Not able to dispatch the event", sb.toString())
            		.map(b -> dto);
		})
		.map(dto -> {
			_LOGGER.error(String.format("Failed to send envent: %s to: %s", dto.getEvent_type().name(), dto.getUrl_listener()));
			return dto;
		}).then();
		// @formatter:on
	}

	private void checkListenerKey(String listenerUrl, EventType event) throws IxigoException {
		if (event == null) {
			throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(ErrorCodes.MISSING_EVENT_TYPE),
					ErrorCodes.MISSING_EVENT_TYPE);
		}

		try {
			new URL(listenerUrl);
		} catch (MalformedURLException e) {
			throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(ErrorCodes.WRONG_URL_FORMAT),
					ErrorCodes.WRONG_URL_FORMAT);
		}
	}
}
