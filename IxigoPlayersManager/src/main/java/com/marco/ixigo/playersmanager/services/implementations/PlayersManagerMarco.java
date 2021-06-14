package com.marco.ixigo.playersmanager.services.implementations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.marco.ixigo.playersmanager.enums.ScoreType;
import com.marco.ixigo.playersmanager.models.dto.Team;
import com.marco.ixigo.playersmanager.models.dto.UserAvgScore;
import com.marco.ixigo.playersmanager.services.interfaces.PlayersManager;
import com.marco.utils.MarcoException;

public class PlayersManagerMarco implements PlayersManager {

    @Override
    public List<Team> generateTeams(Integer teamsCounter, Integer gamesCounter, List<String> usersIDs,
            ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Team> generateTeamsForcingSimilarTeamSizes(Integer teamsCounter, Integer gamesCounter,
            List<String> usersIDs, double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed)
            throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Team> generateTwoTeamsForcingSimilarTeamSizes(Integer gamesCounter, List<String> usersIDs,
            double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, UserAvgScore> getUsersAvgStatsForLastXGames(Integer gamesCounter, List<String> usersIDs,
            ScoreType partionByScore, BigDecimal minPercPlayed) throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }

}
