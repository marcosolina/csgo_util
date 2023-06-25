package com.ixigo.demmanager.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanager.config.properties.NotificationEndpoints;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.services.interfaces.NotificationService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.notification.model.rest.telegram.RestTelegramMessage;

import reactor.core.publisher.Mono;

/**
 * Telgram implementation of the @NotificationService
 * 
 * @author marco
 *
 */
public class TelegramNotificationService implements NotificationService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(TelegramNotificationService.class);
	@Autowired
	private NotificationEndpoints props;
	@Autowired
	private IxigoWebClientUtils webClient;

	@Override
	public Mono<Boolean> sendParsingCompleteNotification(String title, String message) {

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
