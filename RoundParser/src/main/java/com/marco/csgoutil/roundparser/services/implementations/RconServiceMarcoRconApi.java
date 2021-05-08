package com.marco.csgoutil.roundparser.services.implementations;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.marco.csgoutil.roundparser.config.properties.RconProperties;
import com.marco.csgoutil.roundparser.model.rest.rcon.RconResponse;
import com.marco.csgoutil.roundparser.model.service.RconCmd;
import com.marco.csgoutil.roundparser.services.interfaces.RconService;
import com.marco.utils.MarcoException;
import com.marco.utils.network.MarcoNetworkUtils;

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
	private MarcoNetworkUtils mnu;

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
			
			URL url = new URL(rconProps.getProtocol(), rconProps.getHost(), rconProps.getEndpoint());
			ClientResponse resp = mnu.performPostRequest(url, Optional.empty(), Optional.of(rconApiRequest));
			RconResponse rconResp = mnu.getBodyFromResponse(resp, RconResponse.class);
			
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

}
