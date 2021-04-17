package com.marco.csgoutil.roundparser.services.interfaces;

import java.util.List;

import com.marco.utils.MarcoException;

/**
 * This service will allow you to comunicate with the CSGO rcon console
 * 
 * @author Marco
 *
 */
public interface RconService {

	/**
	 * It will move the specified users into the terrorist team and all the remaing
	 * into the CT team
	 * 
	 * @param terroristSteamIDs
	 * @return
	 * @throws MarcoException
	 */
	public boolean moveSpecifiedPlayerToTteam(List<String> terroristSteamIDs) throws MarcoException;
}
