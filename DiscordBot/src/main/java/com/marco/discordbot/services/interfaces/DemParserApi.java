package com.marco.discordbot.services.interfaces;

import java.util.List;
import java.util.Map;

import com.marco.discordbot.enums.TeamType;
import com.marco.discordbot.model.rest.roundparser.Teams;
import com.marco.discordbot.model.rest.roundparser.User;
import com.marco.utils.MarcoException;

public interface DemParserApi {

    /**
     * It performs a REST call to the Round Parser service to retrieve a list of the
     * users currently playing on the IxiGo Server
     * 
     * @return
     */
    public Map<TeamType, List<User>> getCurrentActivePlayersOnTheIxiGoServer() throws MarcoException;

    /**
     * It generate balanced teams using the list of currently active players on the
     * IxiGo Server
     * 
     * @return
     */
    public Teams generateCsgoTeams() throws MarcoException;

    /**
     * It will generate the IxiGo teams based on the currently active players on the
     * server and then move the players to the appropriate CSGO team
     * 
     * @return
     */
    public boolean balanceTheTeams() throws MarcoException;

    /**
     * It will restart the server round
     * 
     * @return
     */
    public boolean restartIxiGoRound() throws MarcoException;
}
