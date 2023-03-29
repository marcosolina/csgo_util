package com.ixigo.eventdispatcher.models.database;

import java.time.LocalDateTime;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.library.dto.IxigoDto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class Event_listenersDto implements IxigoDto{

	private static final long serialVersionUID = 1L;
	private String url_listener = "";
	private EventType event_type;
	private LocalDateTime last_failure = null;
	private Integer consecutive_failure = null;
	private String active = "";
	private LocalDateTime last_successful = null;

	public String getUrl_listener(){
		return this.url_listener;
	}

	public void setUrl_listener(String url_listener){
		this.url_listener = url_listener;
	}


	public EventType getEvent_type(){
		return this.event_type;
	}

	public void setEvent_type(EventType event_type){
		this.event_type = event_type;
	}


	public LocalDateTime getLast_failure(){
		return this.last_failure;
	}

	public void setLast_failure(LocalDateTime last_failure){
		this.last_failure = last_failure;
	}


	public Integer getConsecutive_failure(){
		return this.consecutive_failure;
	}

	public void setConsecutive_failure(Integer consecutive_failure){
		this.consecutive_failure = consecutive_failure;
	}


	public String getActive(){
		return this.active;
	}

	public void setActive(String active){
		this.active = active;
	}


	public LocalDateTime getLast_successful(){
		return this.last_successful;
	}

	public void setLast_successful(LocalDateTime last_successful){
		this.last_successful = last_successful;
	}

	@Override
	public String toString() {
		return "Event_listenersDto [url_listener=" + url_listener + ", event_type=" + event_type + "]";
	}
}
