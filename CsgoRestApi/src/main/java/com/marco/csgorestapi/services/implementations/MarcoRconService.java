package com.marco.csgorestapi.services.implementations;

import java.io.IOException;

import com.marco.csgorestapi.rcon.Rcon;
import com.marco.csgorestapi.rcon.exceptions.AuthenticationException;
import com.marco.csgorestapi.services.interfaces.RconService;
import com.marco.utils.MarcoException;

public class MarcoRconService implements RconService {

	@Override
	public String sendRconCommand(String host, int port, String rconPassw, String rconCmd) throws MarcoException {

		Rcon rcon = null;
		try {
			rcon = new Rcon(host, port, rconPassw.getBytes());
			return rcon.command(rconCmd);
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
