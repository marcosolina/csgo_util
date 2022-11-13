package com.ixigo.rconapi.services.implementations;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.GameServer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.rconapi.services.interfaces.RconService;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class RconServiceSteamCondenser implements RconService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RconServiceSteamCondenser.class);
	@Autowired
	private IxigoMessageResource msgSource;

	@Override
	public Mono<String> sendRconCommand(String host, int port, String rconPassw, String rconCmd) throws IxigoException {
		String errorCode = null;
		if (host == null || host.isEmpty()) {
			errorCode = "RCON00001";
		}
		if (port == 0) {
			errorCode = "RCON00002";
		}
		if (rconPassw == null || rconPassw.isEmpty()) {
			errorCode = "RCON00003";
		}
		if (rconCmd == null || rconCmd.isEmpty()) {
			errorCode = "RCON00004";
		}
		
		if(errorCode != null) {
			return Mono.error(new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(errorCode), errorCode));
		}
		return Mono.fromSupplier(() -> {
			try {
				GameServer server = new SourceServer(host, port);
				LOGGER.debug("Authenticating on the server");
				server.rconAuth(rconPassw);
				LOGGER.debug(String.format("Sending RCON command: %s", rconCmd));
				String response = server.rconExec(rconCmd);
				LOGGER.debug(String.format("RCON response: %s", response));
				return response;
			} catch (SteamCondenserException | TimeoutException e) {
				throw new IxigoException(HttpStatus.BAD_GATEWAY, e.getMessage(), "RCON00005");
			}
		}).subscribeOn(Schedulers.boundedElastic());
	}

}
