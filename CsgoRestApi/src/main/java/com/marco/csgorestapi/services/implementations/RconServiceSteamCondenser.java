package com.marco.csgorestapi.services.implementations;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.GameServer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.marco.csgorestapi.services.interfaces.RconService;
import com.marco.utils.MarcoException;

public class RconServiceSteamCondenser implements RconService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RconServiceSteamCondenser.class);

	@Override
	public String sendRconCommand(String host, int port, String rconPassw, String rconCmd) throws MarcoException {
		try {
			GameServer server = new SourceServer(host, port);
			LOGGER.debug("Authenticating on the server");
			server.rconAuth(rconPassw);
			LOGGER.debug(String.format("Sending RCON command: %s", rconCmd));
			String response = server.rconExec(rconCmd);
			LOGGER.debug(String.format("RCON response: %s", response));
			return response;
		} catch (SteamCondenserException | TimeoutException e) {
			throw new MarcoException(e);
		}
	}
}
