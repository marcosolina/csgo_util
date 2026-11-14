package com.ixigo.library.rest.interfaces;

import com.ixigo.eventdispatcher.enums.EventType;

import reactor.core.publisher.Mono;

public interface IxigoEventSender {
	public Mono<Boolean> sendIxigoEvent(EventType eventType);
}
