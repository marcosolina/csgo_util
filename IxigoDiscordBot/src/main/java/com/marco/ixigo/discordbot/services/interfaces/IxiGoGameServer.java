package com.marco.ixigo.discordbot.services.interfaces;

import java.util.List;
import java.util.Map;

import com.marco.ixigo.discordbot.enums.TeamType;
import com.marco.ixigo.discordbot.model.csgogame.User;
import com.marco.ixigo.discordbot.model.playersmanager.Teams;
import com.marco.utils.MarcoException;

public interface IxiGoGameServer {
    /**
     * It performs a REST call to the RCON service to retrieve a list of the users
     * currently playing on the IxiGo Server
     * 
     * @return
     */
    public Map<TeamType, List<User>> getCurrentActivePlayersOnTheIxiGoServer() throws MarcoException;

    /**
     * It will call the Players Manager Service to generate balanced teams using the
     * list of currently active players on the IxiGo Server
     * 
     * @return
     */
    public Teams generateCsgoTeams() throws MarcoException;

    /**
     * It will move the players to the appropriate CSGO team
     * 
     * @return
     */
    public boolean movePlayersToAppropriateTeam(Teams teams) throws MarcoException;

    /**
     * It will restart the server round
     * 
     * @return
     */
    public boolean restartIxiGoRound() throws MarcoException;
}
