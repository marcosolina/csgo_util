package com.ixigo.demmanager.repositories.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.models.database.Player_match_stats_extendedDao;
import com.ixigo.demmanager.models.database.Player_match_stats_extendedDto;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;

import reactor.core.publisher.Flux;

public class RepoUserScorePostgres implements RepoUserScore {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoUserScorePostgres.class);

	@Autowired
	private DatabaseClient client;

	@Override
	public Flux<Player_match_stats_extendedDto> getLastXMatchesScoresForUser(Integer numberOfMatches, String steamID) {
		_LOGGER.trace("Inside RepoUserScorePostgres.getLastXMatchesScoresForUser");

		Player_match_stats_extendedDao dao = new Player_match_stats_extendedDao();

		StringBuilder sql = new StringBuilder();
		sql.append("select * from ");
		sql.append(Player_match_stats_extendedDao.tableName);
		sql.append(" where ");
		sql.append(Player_match_stats_extendedDto.Fields.steamid);
		sql.append(" =:" + Player_match_stats_extendedDto.Fields.steamid);
		// sql.append(" and ");
		// sql.append(Users_scoresDto.Fields.mp);
		// sql.append(" >=:" + Users_scoresDto.Fields.mp);
		sql.append(" order by ");
		sql.append(Player_match_stats_extendedDto.Fields.match_date);
		sql.append(" desc ");
		sql.append(" limit :limit ");

		// @formatter:off
		return client.sql(sql.toString())
			.bind(Player_match_stats_extendedDto.Fields.steamid, steamID)
			//.bind(Users_scoresDto.Fields.mp, minPercPlayed)
			.bind("limit", numberOfMatches)
			.map(dao::mappingFunction)
			.all();
		// @formatter:on
	}

}
