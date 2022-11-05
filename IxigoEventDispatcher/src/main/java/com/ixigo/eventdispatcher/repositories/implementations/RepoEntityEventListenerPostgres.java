package com.ixigo.eventdispatcher.repositories.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.eventdispatcher.models.database.Event_listenersDao;
import com.ixigo.eventdispatcher.models.database.Event_listenersDto;
import com.ixigo.eventdispatcher.repositories.interfaces.RepoEntityEventListener;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RepoEntityEventListenerPostgres implements RepoEntityEventListener {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoEntityEventListenerPostgres.class);

	@Autowired
	private DatabaseClient client;

	@Override
	public Mono<Boolean> registerNewListener(Event_listenersDto dto) {
		_LOGGER.trace("Inside RepoEntityEventListenerPostgres.registerNewListener");
		Event_listenersDao dao = new Event_listenersDao();
		dao.setDto(dto);
		return dao.prepareSqlInsert(client).then().thenReturn(true);
	}

	@Override
	public Mono<Boolean> updateEntity(Event_listenersDto dto) {
		_LOGGER.trace("Inside RepoEntityEventListenerPostgres.updateEntity");
		Event_listenersDao dao = new Event_listenersDao();
		dao.setDto(dto);
		return dao.prepareSqlUpdate(client).then().thenReturn(true);
	}

	@Override
	public Flux<Event_listenersDto> getListernersOfEvent(EventType event) {
		_LOGGER.trace("Inside RepoEntityEventListenerPostgres.getListernersOfEvent");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ");
		sql.append(Event_listenersDao.tableName);
		sql.append(" WHERE ");
		sql.append(Event_listenersDto.Fields.consecutive_failure + " < 3");
		sql.append(" AND ");
		sql.append(Event_listenersDto.Fields.active + " = 'N'");
		sql.append(" AND ");
		sql.append(Event_listenersDto.Fields.event_type + " = :" + Event_listenersDto.Fields.event_type);

		// @formatter:off
		return client.sql(sql.toString())
				.bind(Event_listenersDto.Fields.event_type, event.toString())
				.map(new Event_listenersDao()::mappingFunction)
				.all();
		// @formatter:on
	}

	@Override
	public Mono<Boolean> deleteListener(String url, EventType eventType) {
		_LOGGER.trace("Inside RepoEntityEventListenerPostgres.deleteListener");
		Event_listenersDao dao = new Event_listenersDao();
		dao.setUrl_listener(url);
		dao.setEvent_type(eventType);
		return dao.prepareSqlDeleteByKey(client).then().thenReturn(true);
	}

	@Override
	public Mono<Event_listenersDto> findListener(String url, EventType eventType) {
		_LOGGER.trace("Inside RepoEntityEventListenerPostgres.findListener");
		Event_listenersDao dao = new Event_listenersDao();
		dao.setUrl_listener(url);
		dao.setEvent_type(eventType);
		return dao.prepareSqlSelectByKey(client).map(dao::mappingFunction).one();
	}

}
