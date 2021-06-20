package com.marco.ixigo.playersmanager.services.implementations.partitionmanager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.marco.ixigo.playersmanager.ixigopartitionlibrary.PartitionTwoTeams;
import com.marco.ixigo.playersmanager.ixigopartitionlibrary.Subset;
import com.marco.ixigo.playersmanager.models.dto.Team;
import com.marco.ixigo.playersmanager.models.dto.UserAvgScore;
import com.marco.ixigo.playersmanager.services.interfaces.PartitionTeams;
import com.marco.utils.MarcoException;

public class PartitionTeamsIxigo implements PartitionTeams {

    private static final Logger _LOGGER = LoggerFactory.getLogger(PartitionTeamsIxigo.class);
    @Autowired
    private MessageSource msgSource;

    @Override
    public List<Team> partitionTheUsersComparingTheScores(List<UserAvgScore> usersList, Integer partions,
            double penaltyWeight) throws MarcoException{
        
        if(penaltyWeight == 0) {
            throw new MarcoException(msgSource.getMessage("DEMP00002", null, LocaleContextHolder.getLocale()));
        }
        if (penaltyWeight < 0) {
            throw new MarcoException(msgSource.getMessage("DEMP00003", null, LocaleContextHolder.getLocale()));
        }

        usersList.sort((o1, o2) -> o1.getTeamSplitScore().compareTo(o2.getTeamSplitScore()) * -1);
        Map<Integer, UserAvgScore> userMap = new HashMap<>();

        StringBuilder sbScores = new StringBuilder();
        StringBuilder sbIndex = new StringBuilder();
        sbScores.append("Scores:  ");
        sbIndex.append("Indexes: ");
        int i = 1;
        for (UserAvgScore userAvgScore : usersList) {
            userMap.put(i, userAvgScore);

            sbScores.append(userAvgScore.getTeamSplitScore());
            sbScores.append(" ");
            sbIndex.append("  " + i + "   ");
            i++;
        }

        List<Double> scores = usersList.stream().map(u -> u.getTeamSplitScore().doubleValue()).collect(Collectors.toList());

        PartitionTwoTeams ep = new PartitionTwoTeams(scores, penaltyWeight);
        if (_LOGGER.isDebugEnabled()) {
            _LOGGER.debug(sbScores.toString());
            _LOGGER.debug(sbIndex.toString());
            ep.print(true, 0);
        }

        List<Subset> subsetsList = ep.getSubsets();
        List<Team> listTeams = new ArrayList<>();

        subsetsList.forEach(s -> {
            Team t = new Team();
            t.setTeamScore(BigDecimal.valueOf(s.getSumVal()).setScale(2, RoundingMode.DOWN));
            s.getNumbIDs().forEach(numId -> t.addMember(userMap.get(numId)));
            listTeams.add(t);
        });

        return listTeams;
    }

    @Override
    public List<Team> partitionTheUsersComparingTheScoresAndTeamMembers(List<UserAvgScore> usersList, Integer partions,
            double penaltyWeight) throws MarcoException{

        return partitionTheUsersComparingTheScores(usersList, partions, penaltyWeight);

    }

}
