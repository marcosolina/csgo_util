package com.ixigo.eventdispatcher.models.database;

import java.time.LocalDateTime;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Event_listenersDao extends IxigoDao<Event_listenersDto> {

	private static final long serialVersionUID = 1L;
	public static final String tableName="event_listeners";
	private Event_listenersDto dto = null;

	public Event_listenersDao() {
		this.setSqlViewName(tableName);
		this.setSqlKeys(new String[] { Event_listenersDto.Fields.url_listener, Event_listenersDto.Fields.event_type });
		// @formatter:off
		this.setSqlFields(new String[] {
			Event_listenersDto.Fields.url_listener,
			Event_listenersDto.Fields.event_type,
			Event_listenersDto.Fields.last_failure,
			Event_listenersDto.Fields.consecutive_failure,
			Event_listenersDto.Fields.active,
			Event_listenersDto.Fields.last_successful
		});
		// @formatter:on
		this.dto = new Event_listenersDto();
	}
	
	@Override
	public Event_listenersDto mappingFunction(Row row, RowMetadata rowMetaData) {
		Event_listenersDto dto = new Event_listenersDto();
		dto.setEvent_type(EventType.valueOf(row.get(Event_listenersDto.Fields.event_type, String.class)));
		dto.setActive(row.get(Event_listenersDto.Fields.active, String.class));
		dto.setConsecutive_failure(row.get(Event_listenersDto.Fields.consecutive_failure, Long.class));
		dto.setLast_failure(row.get(Event_listenersDto.Fields.last_failure, LocalDateTime.class));
		dto.setLast_successful(row.get(Event_listenersDto.Fields.last_successful, LocalDateTime.class));
		dto.setUrl_listener(row.get(Event_listenersDto.Fields.url_listener, String.class));
		return dto;
	}

	public String getUrl_listener() {
		return dto.getUrl_listener();
	}

	public void setUrl_listener(String url_listener) {
		this.dto.setUrl_listener(url_listener);
	}

	public EventType getEvent_type() {
		return dto.getEvent_type();
	}

	public void setEvent_type(EventType event_type) {
		this.dto.setEvent_type(event_type);
	}

	public LocalDateTime getLast_failure() {
		return dto.getLast_failure();
	}

	public void setLast_failure(LocalDateTime last_failure) {
		this.dto.setLast_failure(last_failure);
	}

	public Long getConsecutive_failure() {
		return dto.getConsecutive_failure();
	}

	public void setConsecutive_failure(Long consecutive_failure) {
		this.dto.setConsecutive_failure(consecutive_failure);
	}

	public String getActive() {
		return dto.getActive();
	}

	public void setActive(String active) {
		this.dto.setActive(active);
	}

	public LocalDateTime getLast_successful() {
		return dto.getLast_successful();
	}

	public void setLast_successful(LocalDateTime last_successful) {
		this.dto.setLast_successful(last_successful);
	}

	public Event_listenersDto getDto() {
		return this.dto;
	}

	public void setDto(Event_listenersDto dto) {
		this.dto = dto;
	}

}
