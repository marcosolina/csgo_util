package com.ixigo.demmanager.services.implementations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.models.svc.demdata.SvcMapPlayedCounter;
import com.ixigo.demmanager.models.svc.demdata.SvcMapStats;
import com.ixigo.demmanager.models.svc.demdata.SvcUser;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.ixigo.demmanager.repositories.interfaces.RepoUser;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanager.services.interfaces.DemProcessor;
import com.ixigo.demmanager.services.interfaces.NotificationService;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DemFileParserImp implements DemFileParser {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemFileParserImp.class);

	@Autowired
	private DemFileManagerProps props;
	@Autowired
	private RepoUser repoUser;
	@Autowired
	private DemProcessor svcDemProcessor;
	@Autowired
	private RepoProcessQueue repoQueue;
	@Autowired
	private NotificationService notificationService;

	@Override
	public Mono<Boolean> processFiles() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> processAllFiles() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Map<String, String>> mapOfAvailableScores() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<SvcMapPlayedCounter> countGamesOnAMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<SvcUser> getListOfUsers() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Map<String, List<SvcMapStats>>> getUsersStatsForLastXGames(Integer gamesCounter, List<String> usersIDs, BigDecimal minPercPlayed) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

}
