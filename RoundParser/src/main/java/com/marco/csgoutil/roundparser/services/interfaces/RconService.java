package com.marco.csgoutil.roundparser.services.interfaces;

import java.util.List;
import java.util.Map;

import com.marco.csgoutil.roundparser.enums.TeamType;
import com.marco.csgoutil.roundparser.model.rest.players.User;
import com.marco.csgoutil.roundparser.model.rest.rcon.RconResponse;
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

    /**
     * It returns the current ingame players
     * 
     * @return
     * @throws MarcoException
     */
    public Map<TeamType, List<User>> getCurrentPlayersIds() throws MarcoException;

    /**
     * It restarts the IxiGo Round
     * 
     * @return
     * @throws MarcoException
     */
    public RconResponse restartTheRound() throws MarcoException;
}
