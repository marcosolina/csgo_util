package com.marco.csgoutil.roundparser.services.implementations;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.util.UriBuilder;

import com.marco.csgoutil.roundparser.config.properties.RconProperties;
import com.marco.csgoutil.roundparser.model.rest.rcon.RconResponse;
import com.marco.csgoutil.roundparser.model.service.RconCmd;
import com.marco.csgoutil.roundparser.services.interfaces.RconService;
import com.marco.utils.MarcoException;

/**
 * This implementation provides the logic used to send CSGO RCON command to my
 * RCON service
 * <ul>
 * <li><a href="https://marco.selfip.net/rcon/swagger-ui.html">RCON API</a></li>
 * <li><a href=
 * "https://github.com/marcosolina/csgo_util/tree/main/CsgoRestApi">Github
 * project</a></li>
 * </ul>
 * 
 * @author Marco
 * 
 */
public class RconServiceMarcoRconApi implements RconService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RconServiceMarcoRconApi.class);
	@Autowired
	private RconProperties rconProps;
	@Autowired
	private WebClient.Builder wcb;

	@Override
	public boolean moveSpecifiedPlayerToTteam(List<String> terroristSteamIDs) throws MarcoException {
		_LOGGER.debug(rconProps.getEndpoint());

		_LOGGER.debug("Calling rcon API");
		try {

			StringBuilder sb = new StringBuilder();
			// @formatter:off
			terroristSteamIDs.stream().forEach(steamId -> {
				sb.append(" \"");
				sb.append(steamId);
				sb.append("\"");
			});
			// @formatter:on

			RconCmd rconApiRequest = new RconCmd();
			rconApiRequest.setRconCmd(String.format("sm_move_players %s dummy", sb.toString().substring(1)));
			rconApiRequest.setRconHost(rconProps.getCsgoserverip());
			rconApiRequest.setRconPass(rconProps.getCsgorconpassw());
			rconApiRequest.setRconPort(27015);
			
			String stringUrl = rconProps.getProtocol() + rconProps.getIp() + rconProps.getEndpoint();
			if(_LOGGER.isDebugEnabled()) {
				_LOGGER.debug(String.format("Url RCON: %s", stringUrl));
			}
			
			URL url = new URL(stringUrl);
			ClientResponse resp = performRequest(HttpMethod.POST, url, null, null, MediaType.APPLICATION_JSON, rconApiRequest);
			RconResponse rconResp = getBodyFromResponse(resp, RconResponse.class);
			
			if(_LOGGER.isDebugEnabled()) {
				_LOGGER.debug(String.format("RCON Resp status: %s", rconResp.isStatus()));
				_LOGGER.debug(String.format("RCON Resp message: %s", rconResp.getRconResponse()));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarcoException(e);
		}
	}

	@SuppressWarnings("deprecation")
	private ClientResponse performRequest(HttpMethod method, URL url, Map<String, String> headers,
			Map<String, String> queryParameters, MediaType contentType, Object body) {

		/*
		 * Create the request and adds query parameters if provided
		 */
		RequestBodySpec rbs = wcb.build().method(method).uri(uriBuilder -> {
			UriBuilder ub = uriBuilder
					.scheme(url.getProtocol())
					.host(url.getHost())
					.port(url.getPort())
					.path(url.getPath());
			if (queryParameters != null) {
				for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
					ub = ub.queryParam(entry.getKey(), entry.getValue());
				}
			}
			return ub.build();

		}).contentType(contentType);

		/*
		 * Add HTTP headers if provided
		 */
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				rbs = rbs.header(entry.getKey(), entry.getValue());
			}
		}

		/*
		 * Perform the call
		 */
		ClientResponse resp = null;
		if (body != null) {
			resp = rbs.bodyValue(body).exchange().block();
		} else {
			resp = rbs.exchange().block();
		}
		return resp;
	}

	private <T> T getBodyFromResponse(ClientResponse response, Class<T> clazz) {
		return response.bodyToMono(clazz).block();
	}

}
