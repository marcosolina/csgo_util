package com.ixigo.eventdispatcher.repositories.interfaces;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.eventdispatcher.models.database.Event_listenersDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepoEntityEventListener {
	/**
	 * It register a new Event Listener
	 * 
	 * @param entity
	 * @return
	 */
	public Mono<Boolean> registerNewListener(Event_listenersDto dro);

	/**
	 * It updates the entity
	 * 
	 * @param entity
	 * @return
	 */
	public Mono<Boolean> updateEntity(Event_listenersDto entity);

	/**
	 * It returns a list of event listeners of the specific event. The list contains
	 * only "active" listeners which hare still responding
	 * 
	 * @param event
	 * @return
	 */
	public Flux<Event_listenersDto> getListernersOfEvent(EventType event);

	/**
	 * Deletes the listener
	 * 
	 * @param entity
	 * @return
	 */
	public Mono<Boolean> deleteListener(String url, EventType eventType);

	/**
	 * It returns the listener definition
	 * 
	 * @param key
	 * @return
	 */
	public Mono<Event_listenersDto> findListener(String url, EventType eventType);
}
