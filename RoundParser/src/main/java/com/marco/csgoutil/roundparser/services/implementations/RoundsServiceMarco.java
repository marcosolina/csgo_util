package com.marco.csgoutil.roundparser.services.implementations;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.marco.csgoutil.roundparser.model.entities.DaoGames;
import com.marco.csgoutil.roundparser.model.entities.EntityUser;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScore;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScorePk;
import com.marco.csgoutil.roundparser.model.rest.User;
import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.csgoutil.roundparser.model.service.UserMapStats;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUser;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUserScore;
import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;
import com.marco.utils.DateUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DateFormats;

public class RoundsServiceMarco implements RoundsService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RoundsServiceMarco.class);

	@Autowired
	private RoundFileService roundFildeService;
	@Autowired
	private CsgoRoundFileParser roundParserService;
	@Autowired
	private RepoUser repoUser;
	@Autowired
	private RepoUserScore repoUserScore;
	@Autowired
	private MessageSource msgSource;

	@Override
	public List<MapStats> processAllDemFiles() throws MarcoException {
		List<File> fileList = roundFildeService.retrieveAllDemFiles();

		List<DaoGames> availableRecordings = repoUserScore.listAvailableGames();

		List<MapStats> mapStats = new ArrayList<>();
		// @formatter:off
		fileList.parallelStream()
			.filter(f -> !availableRecordings.contains(new DaoGames(setMapNameAndTime(f).getPlayedOn())))
			.forEach(f -> {
				try {
					mapStats.add(this.generateMapStatFromFile(f));
				} catch (MarcoException e) {
					_LOGGER.error(String.format("Could not process DEM file: %s", f.getAbsoluteFile()));
					_LOGGER.info(String.format("File deleted: %b", f.delete()));
				}
			});
		// @formatter:on

		mapStats.sort((o1, o2) -> o1.getPlayedOn().compareTo(o2.getPlayedOn()));

		// @formatter:off
		mapStats.stream().forEach(m -> 
			m.getUsersStats().stream().forEach(u -> {
				EntityUser user = new EntityUser();
				user.setSteamId(u.getSteamID());
				user.setUserName(u.getUserName());
				repoUser.insertUpdateUser(user);
				
				EntityUserScore us = new EntityUserScore();
				us.setScore(Long.valueOf(u.getScore()));
				
				EntityUserScorePk usId = new EntityUserScorePk();
				usId.setMap(m.getMapName());
				usId.setGameDate(m.getPlayedOn());
				usId.setSteamId(u.getSteamID());
				us.setId(usId);
				
				repoUserScore.insertUpdateUserScore(us);
			})
		);
		// @formatter:on

		/*
		Map<String, Double> avarageMap = new HashMap<>();

		List<Map<String, Integer>> listMap = new ArrayList<>();
		
		Map<String, Integer> userGamesPlayer = new HashMap<>();
		
		fileList.parallelStream().forEach(f -> {
			try {
				listMap.add(roundParserService.extractPlayersScore(f));
			} catch (MarcoException e) {
				_LOGGER.error(String.format("Could not process DEM file: %s", f.getAbsoluteFile()));
			}
		});
		
		int size = listMap.size();
		if(size > 0) {
			// @formatter:off
			listMap.stream().forEach(m -> 
				m.keySet().stream().forEach(k -> {
					userGamesPlayer.compute(k , (key, v) -> v == null ? 1 : v + 1);
					avarageMap.compute(k, (key, v) -> {
						if(v == null) {
							v = Double.valueOf(0);
						}
						v = v + Double.valueOf(m.get(key));
						return v;
					});
				})
			);
			// @formatter:on
			avarageMap.keySet().stream().forEach(k -> avarageMap.compute(k, (key, v) -> v / userGamesPlayer.get(key)));
		}*/

		return mapStats.stream().filter(e -> !e.getUsersStats().isEmpty()).collect(Collectors.toList());
	}

	@Override
	public MapStats generateMapStatFromFile(File f) throws MarcoException {
		MapStats ms = setMapNameAndTime(f);
		ms.setUsersStats(roundParserService.extractPlayersScore(f));
		return ms;
	}

	private MapStats setMapNameAndTime(File f) {
		String[] tmp = f.getName().split("-");

		LocalDateTime ldt = DateUtils.fromStringToLocalDateTime(tmp[1] + "_" + tmp[2], DateFormats.FILE_NAME);
		MapStats ms = new MapStats();
		ms.setPlayedOn(ldt);
		ms.setMapName(tmp[4]);
		return ms;
	}

	@Override
	public List<User> getListOfUsers() throws MarcoException {
		return repoUser.getUsers().stream().map(this::fromEntityUserToRestUser).collect(Collectors.toList());
	}

	private User fromEntityUserToRestUser(EntityUser entity) {
		User u = new User();
		u.setSteamId(entity.getSteamId());
		u.setUserName(entity.getUserName());
		return u;
	}

	@Override
	public List<MapStats> getUserStats(String steamId) throws MarcoException {
		EntityUser user = repoUser.findById(steamId);
		if(user == null) {
			throw new MarcoException(msgSource.getMessage("DEMP00001", null, LocaleContextHolder.getLocale()));
		}
		
		List<EntityUserScore> scores = repoUserScore.getUserScores(steamId);
		
		return scores.stream().map(s -> fromDbDataToMapStats(user, s)).collect(Collectors.toList());
	}
	
	private MapStats fromDbDataToMapStats(EntityUser user, EntityUserScore score) {
		MapStats ms = new MapStats();
		ms.setMapName(score.getId().getMap());
		ms.setPlayedOn(score.getId().getGameDate());
		
		UserMapStats ums = new UserMapStats();
		ums.setScore(score.getScore().intValue());
		ums.setSteamID(user.getSteamId());
		ums.setUserName(user.getUserName());
		ms.addUserMapStats(ums);
		
		return ms;
	}
	
	

}
