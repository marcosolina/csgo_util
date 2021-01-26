package com.marco.csgorestapi.services.interfaces;

import com.marco.utils.MarcoException;

public interface RconService {

	public String sendRconCommand(String host, int port, String rconPassw, String rconCmd) throws MarcoException;
}
