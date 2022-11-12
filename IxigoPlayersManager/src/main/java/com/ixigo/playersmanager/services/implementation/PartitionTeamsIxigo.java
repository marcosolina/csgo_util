package com.ixigo.playersmanager.services.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.partitioning.IxigoEfficientPartition;
import com.ixigo.playersmanager.models.svc.SvcTeam;
import com.ixigo.playersmanager.models.svc.SvcUserAvgScore;
import com.ixigo.playersmanager.services.interfaces.PartitionTeams;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

public class PartitionTeamsIxigo implements PartitionTeams {
	private static final Logger _LOGGER = LoggerFactory.getLogger(PartitionTeamsIxigo.class);
	@Autowired
	private IxigoMessageResource msgSource;

	@Override
	public Flux<SvcTeam> partitionTheUsersIntoTwoTeams(Flux<SvcUserAvgScore> usersList, double penaltyWeight) throws IxigoException {
		_LOGGER.trace("Inside PartitionTeamsIxigo.partitionTheUsersIntoTwoTeams");
		if(penaltyWeight <= 0) {
			throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage("PM00001"), "PM00001");
        }
		
		// @formatter:off
		return usersList
			.sort((o1, o2) -> o1.getTeamSplitScore().compareTo(o2.getTeamSplitScore()) * -1)
			.map(score -> Tuples.of(score, score.getTeamSplitScore().doubleValue()))
			.collectList()
			.map(tupleList -> {
				var steamIds = tupleList.stream().map(t -> t.getT1()).collect(Collectors.toList());
				var scores = tupleList.stream().map(t -> t.getT2()).collect(Collectors.toList());
				return Tuples.of(steamIds, new IxigoEfficientPartition(scores, penaltyWeight));
			})
			.map(tuple -> Tuples.of(tuple.getT1(), tuple.getT2().getSubsets()))
			.map(tuple -> {
				var usersAvgScores = tuple.getT1();
				var subsetsList = tuple.getT2();
				
				List<SvcTeam> listTeams = new ArrayList<>();
				subsetsList.forEach(s -> {
		            SvcTeam t = new SvcTeam();
		            t.setTeamScore(BigDecimal.valueOf(s.getSumVal()).setScale(2, RoundingMode.DOWN));
		            s.getNumbIDs().forEach(numId -> {
		            	t.addMember(usersAvgScores.get(numId - 1));
	            	});
		            listTeams.add(t);
		        });
				return listTeams;
			})
			.flatMapMany(Flux::fromIterable);
		// @formatter:on
	}
}
