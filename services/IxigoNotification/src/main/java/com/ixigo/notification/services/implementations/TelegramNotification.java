package com.ixigo.notification.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.library.utils.DateUtils;
import com.ixigo.notification.config.properties.TelegramProperties;
import com.ixigo.notification.constants.ErrorCodes;
import com.ixigo.notification.services.interfaces.NotificationService;

import reactor.core.publisher.Mono;

public class TelegramNotification implements NotificationService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(TelegramNotification.class);
	@Autowired
	private TelegramProperties props;
	@Autowired
	private IxigoWebClientUtils webClient;

	@Override
	public Mono<Boolean> sendNotification(String title, String message) {
		if (!props.isEnabled()) {
			_LOGGER.info("Telegram notifications disabled");
			_LOGGER.info(String.format("Title: %s Message: %s", title, message));
			return Mono.just(true);
		}

		if (props.isAllowNotificationsOnlyOnMonday() && DateUtils.getCurrentUtcDate().getDayOfWeek() != DayOfWeek.MONDAY) {
			_LOGGER.info("Telegram notifications allowed on Monday");
			_LOGGER.info(String.format("Title: %s Message: %s", title, message));
			return Mono.just(true);
		}

		try {
			URL url = new URL(String.format("https://api.telegram.org/bot%s/sendMessage", props.getToken()));
			Map<String, String> queryParam = new HashMap<>();
			queryParam.put("chat_id", props.getChatGroupId());
			queryParam.put("text", String.format("%s%n%n%s", title, message));
			// @formatter:off
			return webClient.performGetRequestNoExceptions(Void.class, url, Optional.empty(), Optional.of(queryParam))
					.map(resp -> resp.getStatusCode().is2xxSuccessful());
			// @formatter:on
		} catch (MalformedURLException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

}
