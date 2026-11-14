package com.ixigo.ui.commands;

import java.util.Map;

import org.springframework.http.HttpMethod;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ForwardHttpReqCmd implements WebCommandRequest<Object> {
	private String requestedUri;
	private String contextPath;
	private HttpMethod httpMethod;
	private Map<String, String> queryParams;
	private Object body;
}
