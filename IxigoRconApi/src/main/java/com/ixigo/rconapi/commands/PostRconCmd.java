package com.ixigo.rconapi.commands;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;
import com.ixigo.rconapi.models.rest.RestRconRequest;
import com.ixigo.rconapi.models.rest.RestRconResponse;

public class PostRconCmd implements WebCommandRequest<RestRconResponse> {
	private RestRconRequest request;

	public PostRconCmd(RestRconRequest request) {
		super();
		this.request = request;
	}

	public RestRconRequest getRequest() {
		return request;
	}

	public void setRequest(RestRconRequest request) {
		this.request = request;
	}

}
