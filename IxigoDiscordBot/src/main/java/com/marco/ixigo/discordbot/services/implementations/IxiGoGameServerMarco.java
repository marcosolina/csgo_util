package com.marco.ixigo.discordbot.services.implementations;

import java.util.List;
import java.util.Map;

import com.marco.ixigo.discordbot.enums.TeamType;
import com.marco.ixigo.discordbot.model.demmanager.User;
import com.marco.ixigo.discordbot.model.playersmanager.Teams;
import com.marco.ixigo.discordbot.services.interfaces.IxiGoGameServer;
import com.marco.utils.MarcoException;

public class IxiGoGameServerMarco implements IxiGoGameServer {

    @Override
    public Map<TeamType, List<User>> getCurrentActivePlayersOnTheIxiGoServer() throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Teams generateCsgoTeams() throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean movePlayersToAppropriateTeam(Teams teams) throws MarcoException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean restartIxiGoRound() throws MarcoException {
        // TODO Auto-generated method stub
        return false;
    }

}
