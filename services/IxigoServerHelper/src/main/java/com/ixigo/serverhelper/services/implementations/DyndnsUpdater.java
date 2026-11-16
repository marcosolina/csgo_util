package com.ixigo.serverhelper.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.common.net.HttpHeaders;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.serverhelper.config.properties.DnsProperties;
import com.ixigo.serverhelper.services.interfaces.DnsUpdater;

/**
 * Points the DynDNS host name at this server's public IP. DynDNS takes the IP
 * from the caller when the request has no "myip" parameter, so no IP lookup is
 * needed. The update runs once the application is ready and is retried every
 * 10 minutes until DynDNS accepts it: the IP does not change while the server
 * runs, and DynDNS treats repeated updates with an unchanged IP as abuse.
 */
public class DyndnsUpdater implements DnsUpdater {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DyndnsUpdater.class);
	private static final String UPDATE_URL = "https://members.dyndns.org/nic/update";

	private final AtomicBoolean updated = new AtomicBoolean(false);

	@Autowired
	private DnsProperties props;
	@Autowired
	private IxigoWebClientUtils webClient;

	@EventListener(ApplicationReadyEvent.class)
	public void updateAtStartup() {
		updateDnsEntry();
	}

	@Override
	@Scheduled(cron = "0 */10 * * * *")
	public void updateDnsEntry() {
		if (!props.isEnabled() || updated.get()) {
			return;
		}

		URL url;
		try {
			url = new URL(UPDATE_URL);
		} catch (MalformedURLException e) {
			_LOGGER.error("Invalid DynDNS URL {}", UPDATE_URL, e);
			return;
		}

		String credentials = String.format("%s:%s", props.getUserName(), props.getDnsKey());
		Map<String, String> headers = new HashMap<>();
		headers.put(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes()));

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("hostname", props.getHostname());

		_LOGGER.info("Updating the DynDNS entry of {}", props.getHostname());
		// @formatter:off
		webClient.performGetRequestNoExceptions(String.class, url, Optional.of(headers), Optional.of(queryParams))
			.subscribe(resp -> {
				String body = resp.getBody() == null ? "" : resp.getBody().trim();
				if (resp.getStatusCode().is2xxSuccessful() && (body.startsWith("good") || body.startsWith("nochg"))) {
					updated.set(true);
					_LOGGER.info("DynDNS updated: {}", body);
				} else {
					_LOGGER.warn("DynDNS update not accepted, retrying in 10 minutes: {} {}", resp.getStatusCode(), body);
				}
			}, e -> _LOGGER.warn("DynDNS update failed, retrying in 10 minutes: {}", e.getMessage()));
		// @formatter:on
	}
}
