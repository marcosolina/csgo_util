package com.marco.csgorestapi.services.implementations;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marco.csgorestapi.rcon.Rcon;
import com.marco.csgorestapi.rcon.exceptions.AuthenticationException;
import com.marco.csgorestapi.services.interfaces.RconService;
import com.marco.utils.MarcoException;

/**
 * My implementation of the RCON service
 * 
 * @author Marco
 *
 */
public class MarcoRconService implements RconService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MarcoRconService.class);

	@Override
	public String sendRconCommand(String host, int port, String rconPassw, String rconCmd) throws MarcoException {

		/*
		 * Some sanity checks
		 */
		if (host == null || host.isBlank()) {
			throw new MarcoException("Host name (or ip) is mandatory");
		}

		if (port == 0) {
			throw new MarcoException("The RCON port can not be zero");
		}

		if (rconPassw == null || rconPassw.isBlank()) {
			throw new MarcoException("The RCON password is mandatory");
		}

		if (rconCmd == null || rconCmd.isBlank()) {
			throw new MarcoException("The RCON command is mandatory");
		}

		Rcon rcon = null;
		try {
			/*
			 * Try to connect to the RCON server
			 */
			rcon = new Rcon(host, port, rconPassw.getBytes());

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Sending Rcon: %s", rconCmd));
			}

			/*
			 * Send the RCON command specified as input parameter
			 */
			String rconResp = rcon.command(rconCmd);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Rcon replied with: %s", rconResp));
			}

			return rconResp;
		} catch (IOException | AuthenticationException e) {
			throw new MarcoException(e);
		} finally {
			if (rcon != null && rcon.getSocket() != null) {
				try {
					rcon.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
