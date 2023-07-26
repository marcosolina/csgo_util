package com.ixigo.discordbot.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.ixigo.discordbot.config.properties.CsgoServerProps;
import com.ixigo.discordbot.config.properties.RconApiEndPoints;
import com.ixigo.discordbot.constants.ErrorCodes;
import com.ixigo.discordbot.enums.TeamType;
import com.ixigo.discordbot.services.interfaces.IxigoRconService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.models.rest.RestUser;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;
import com.ixigo.rconapi.models.rest.RestRconRequest;
import com.ixigo.rconapi.models.rest.RestRconResponse;

import reactor.core.publisher.Mono;

public class IxigoRconServiceImpl implements IxigoRconService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoRconServiceImpl.class);
	@Autowired
	private IxigoWebClientUtils webClient;
	@Autowired
	private RconApiEndPoints endPoints;
	@Autowired
	private CsgoServerProps csgoSrvProps;

	@Override
	public Mono<Map<TeamType, List<RestUser>>> getCurrentActivePlayersOnTheIxiGoServer() throws IxigoException {
		_LOGGER.trace("Inside IxigoRconServiceImpl.getCurrentActivePlayersOnTheIxiGoServer");
		var mono = sendRconCommand("sm_list_players");

		return mono.map(resp -> {
			List<RestUser> terrorists = new ArrayList<>();
			List<RestUser> ct = new ArrayList<>();

			if (resp != null && resp.getBody() != null && !Strings.isNullOrEmpty(resp.getBody().getRconResponse())) {
				String[] lines = resp.getBody().getRconResponse().split("\\n");
				if (lines.length > 2) {
					boolean t = true;
					for (int i = 0; i < lines.length; i++) {
						if (lines[i].equals(TeamType.CT.getDesc())) {
							t = false;
							continue;
						}
						if (lines[i].equals(TeamType.TERRORISTS.getDesc())) {
							t = true;
							continue;
						}

						if (t) {
							terrorists.add(new RestUser(lines[i]));
						} else {
							ct.add(new RestUser(lines[i]));
						}

					}
				}
			}

			Map<TeamType, List<RestUser>> players = new EnumMap<>(TeamType.class);
			players.put(TeamType.TERRORISTS, terrorists);
			players.put(TeamType.CT, ct);
			return players;
		});
	}

	@Override
	public Mono<Boolean> kickTheBots() throws IxigoException {
		_LOGGER.trace("Inside IxigoRconServiceImpl.kickTheBots");
		_LOGGER.debug("Kicking the bots");
		return sendRconCommand("bot_kick").map(resp -> resp.getStatusCode().is2xxSuccessful());
	}

	@Override
	public Mono<Boolean> restartIxiGoMatch() throws IxigoException {
		_LOGGER.trace("Inside IxigoRconServiceImpl.restartIxiGoRound");
		return sendRconCommand("mp_restartgame 5").map(resp -> resp.getStatusCode().is2xxSuccessful());
	}

	@Override
	public Mono<Boolean> movePlayersToAppropriateTeam(RestTeams teams) throws IxigoException {
		// @formatter:off
		var steamIdsList = teams.getTeams()
			.get(0)
			.getMembers()
			.stream()
			.map(m -> m.getSteamID())
			.collect(Collectors.toList());
		
		return sendRconCommand(
				String.format("sm_move_players \"%s\" dummy", String.join("\" \"", steamIdsList))
				).map(resp -> resp.getStatusCode().is2xxSuccessful());
		// @formatter:on
	}

	private Mono<ResponseEntity<RestRconResponse>> sendRconCommand(String cmd) {
		_LOGGER.trace("Inside IxigoRconServiceImpl.sendRconCommand");
		try {
			URL url = new URL(endPoints.getPostCommand());
			var req = this.getRconRequest(cmd);
			return webClient.performPostRequest(RestRconResponse.class, url, Optional.of(req));
		} catch (MalformedURLException e) {
			_LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC_ERROR);
		}
	}

	private RestRconRequest getRconRequest(String cmd) {
		RestRconRequest rcon = new RestRconRequest();
		rcon.setRconHost(csgoSrvProps.getHostName());
		rcon.setRconPort(csgoSrvProps.getPort());
		rcon.setRconPass(csgoSrvProps.getRconPassword());
		rcon.setRconCmd(cmd);
		return rcon;
	}
}
