package com.ixigo.eventdispatcher.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.eventdispatcher.config.properties.NotificationEndpoints;
import com.ixigo.eventdispatcher.constants.ErrorCodes;
import com.ixigo.eventdispatcher.services.interfaces.NotificationService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.notification.model.rest.telegram.RestTelegramMessage;

import reactor.core.publisher.Mono;

public class TelegramNotificationService implements NotificationService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(TelegramNotificationService.class);
	@Autowired
	private NotificationEndpoints props;
	@Autowired
	private IxigoWebClientUtils webClient;

	@Override
	public Mono<Boolean> sendEventServiceError(String title, String message) {

		try {
			URL url = new URL(props.getPostTelegram());

			RestTelegramMessage msg = new RestTelegramMessage();
			msg.setTitle(title);
			msg.setMessage(message);
			
			// @formatter:off
			return webClient.performPostRequest(Void.class, url, Optional.of(msg))
					.map(resp -> resp.getStatusCode().is2xxSuccessful());
			// @formatter:on
		} catch (MalformedURLException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

}
