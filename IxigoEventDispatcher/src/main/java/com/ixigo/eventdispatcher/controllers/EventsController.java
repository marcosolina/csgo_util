package com.ixigo.eventdispatcher.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.eventdispatcher.commands.events.PostEventCmd;
import com.ixigo.eventdispatcher.models.rest.IncomingEventHttpRequest;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/event")
public class EventsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventsController.class);
	private final String LOCALHOST_IPV4 = "127.0.0.1";
	private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

	@Autowired
	private WebMediator mediator;

	@PostMapping
	@ApiOperation(value = "It receives the event from the CSGO server", code = 202)
	public Mono<ResponseEntity<Void>> receiveServerEvent(@RequestBody IncomingEventHttpRequest request, HttpServletRequest httRequest) {
		LOGGER.trace("Inside EventsController.receiveServerEvent");
		return mediator.send(new PostEventCmd(request.getEventName(), getClientIp(httRequest)));
	}

	private String getClientIp(HttpServletRequest request) {

		String ipAddress = request.getRemoteAddr();
		if (ipAddress != null) {
			return ipAddress;
		}
		ipAddress = request.getHeader("X-Forwarded-For");
		if (ipAddress != null && (ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress))) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}

		if (ipAddress != null && (ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress))) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ipAddress != null && (ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress))) {
			ipAddress = request.getRemoteAddr();
			if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
				try {
					InetAddress inetAddress = InetAddress.getLocalHost();
					ipAddress = inetAddress.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}

		if (ipAddress != null && !ipAddress.isEmpty() && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
			ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
		}

		return ipAddress;
	}
}
