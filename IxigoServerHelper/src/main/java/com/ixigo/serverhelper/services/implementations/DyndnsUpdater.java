package com.ixigo.serverhelper.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.common.net.HttpHeaders;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.serverhelper.config.properties.DnsProperties;
import com.ixigo.serverhelper.services.interfaces.DnsUpdater;

public class DyndnsUpdater implements DnsUpdater {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DyndnsUpdater.class);

	@Autowired
	private DnsProperties props;
	@Autowired
	private IxigoWebClientUtils webClient;

	@Override
	@Scheduled(cron = "0 */10 * * * *") // Every 5 minutes
	public void updateDnsEntry() {
		if (props.isEnabled()) {
			try {
				URL urlMyIp = new URL("https://api.my-ip.io/ip");

			// @formatter:off
			webClient.performGetRequestNoExceptions(String.class, urlMyIp, Optional.empty(), Optional.empty())
				.map(resp -> resp.getBody())
				.flatMap(strIp -> {
					try {
						URL urlDynDns = new URL(String.format("https://members.dyndns.org/nic/update"));
						_LOGGER.info(urlDynDns.toString());
						
						String toEncode = String.format("%s:%s", props.getUserName(), props.getDnsKey());
						String encodedStr = Base64.getEncoder().encodeToString(toEncode.getBytes());
						Map<String, String> headers = new HashMap<>();
						headers.put(HttpHeaders.AUTHORIZATION, String.format("Basic %s", encodedStr));
						
						Map<String, String> queryParam = new HashMap<>();
						queryParam.put("hostname", props.getHostname());
						
						return webClient.performGetRequestNoExceptions(String.class, urlDynDns, Optional.of(headers), Optional.of(queryParam));
					} catch (MalformedURLException e) {
						e.printStackTrace();
						throw new IxigoException(HttpStatus.BAD_GATEWAY, e.getMessage(), e.getMessage());
					}
	
				})
				.subscribe(resp -> {
					_LOGGER.info(String.format("DynDns response: %s - %s", resp.getStatusCode().toString(), resp.getBody()));
				});
			// @formatter:on

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

}
