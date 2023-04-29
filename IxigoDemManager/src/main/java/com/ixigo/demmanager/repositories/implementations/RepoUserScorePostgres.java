package com.ixigo.demmanager.repositories.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.DatabaseClient.GenericExecuteSpec;

import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.demmanager.models.database.DtoMapPlayedCounter;
import com.ixigo.demmanager.models.database.DtoTeamAvgScorePerMap;
import com.ixigo.demmanager.models.database.DtoUserAvgScorePerMap;
import com.ixigo.demmanager.models.database.Users_scoresDao;
import com.ixigo.demmanager.models.database.Users_scoresDto;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Postgres implementation for @RepoUserScore
 * 
 * @author marco
 *
 */
public class RepoUserScorePostgres implements RepoUserScore {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoUserScorePostgres.class);

	@Autowired
	private DatabaseClient client;

	@Override
	public Mono<Boolean> insertUpdateUserScore(Users_scoresDto userScore) {
		_LOGGER.trace("Inside RepoUserScorePostgres.insertUpdateUserScore");
		Users_scoresDao dao = new Users_scoresDao();
		dao.setDto(userScore);

		// @formatter:off
		return dao.prepareSqlSelectByKey(client).map(dao::mappingFunction).one()
			.switchIfEmpty(Mono.just(new Users_scoresDto()))
			.flatMap(dto -> {
				// Update
				if (!"".equals(dto.getSteam_id())) {
					return dao.prepareSqlUpdate(client).then().thenReturn(true);
				}
				return dao.prepareSqlInsert(client).then().thenReturn(true);
			});
		// @formatter:on
	}

	@Override
	public Flux<LocalDateTime> listAvailableGames() {
		_LOGGER.trace("Inside RepoUserScorePostgres.listAvailableGames");

		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(Users_scoresDto.Fields.game_date);
		sql.append(" from ");
		sql.append(Users_scoresDao.tableName);
		sql.append(" group by ");
		sql.append(Users_scoresDto.Fields.game_date);
		sql.append(" order by ");
		sql.append(Users_scoresDto.Fields.game_date);
		sql.append(" asc");
		GenericExecuteSpec ges = client.sql(sql.toString());

		return ges.map((Row row, RowMetadata rowMetaData) -> row.get(Users_scoresDto.Fields.game_date, LocalDateTime.class)).all();
	}

	@Override
	public Flux<DtoMapPlayedCounter> getMapsPlayed() {
		_LOGGER.trace("Inside RepoUserScorePostgres.getMapsPlayed");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a." + Users_scoresDto.Fields.map + ", COUNT(*) as count");
		sql.append(" FROM ");
		sql.append(" (");
		sql.append("     SELECT " + Users_scoresDto.Fields.game_date + ", " + Users_scoresDto.Fields.map);
		sql.append("     FROM ");
		sql.append(Users_scoresDao.tableName);
		sql.append("     GROUP BY");
		sql.append("     " + Users_scoresDto.Fields.game_date + ", " + Users_scoresDto.Fields.map);
		sql.append(" ) as a");
		sql.append(" GROUP BY ");
		sql.append(Users_scoresDto.Fields.map);
		sql.append(" order by count desc ");
		GenericExecuteSpec ges = client.sql(sql.toString());

		return ges.map((Row row, RowMetadata rowMetaData) -> {
			return new DtoMapPlayedCounter(row.get(Users_scoresDto.Fields.map, String.class), row.get("count", Long.class));
		}).all();
	}

	@Override
	public Flux<Users_scoresDto> getUserScores(String steamID) {
		_LOGGER.trace("Inside RepoUserScorePostgres.getUserScores");
		Users_scoresDao dao = new Users_scoresDao();
		dao.addSqlWhereAndClauses(Users_scoresDto.Fields.steam_id);
		dao.addSqlParams(steamID);
		dao.addSqlOrderFields(Users_scoresDto.Fields.steam_id);

		return dao.prepareSqlSelect(client).map(dao::mappingFunction).all();
	}

	@Override
	public Flux<Users_scoresDto> getLastXMatchesScoresForUser(Integer numberOfMatches, String steamID, BigDecimal minPercPlayed) {
		_LOGGER.trace("Inside RepoUserScorePostgres.getLastXUserScores");

		Users_scoresDao dao = new Users_scoresDao();

		StringBuilder sql = new StringBuilder();
		sql.append("select * from ");
		sql.append(Users_scoresDao.tableName);
		sql.append(" where ");
		sql.append(Users_scoresDto.Fields.steam_id);
		sql.append(" =:" + Users_scoresDto.Fields.steam_id);
		sql.append(" and ");
		sql.append(Users_scoresDto.Fields.mp);
		sql.append(" >=:" + Users_scoresDto.Fields.mp);
		sql.append(" order by ");
		sql.append(Users_scoresDto.Fields.game_date);
		sql.append(" desc ");
		sql.append(" limit :limit ");

		// @formatter:off
		return client.sql(sql.toString())
			.bind(Users_scoresDto.Fields.steam_id, steamID)
			.bind(Users_scoresDto.Fields.mp, minPercPlayed)
			.bind("limit", numberOfMatches)
			.map(dao::mappingFunction)
			.all();
		// @formatter:on
	}
	
	@Override
	public Flux<DtoUserAvgScorePerMap> getUserAveragaScorePerMap(String steamId, ScoreType scoreType, Optional<List<String>> maps, Optional<Integer> lastMatchesToConsider) {
		if(!maps.isPresent() || maps.get().isEmpty()) {
			return this.getMapsPlayed().map(m -> m.getMapName()).collectList().flatMap(list -> {
				return getUserAveragaScorePerMaps(steamId, scoreType, Optional.of(list), lastMatchesToConsider).collectList();
			}).flatMapMany(l -> Flux.fromIterable(l));			
		}
		return getUserAveragaScorePerMaps(steamId, scoreType, maps, lastMatchesToConsider);
	}
	
	@Override
	public Flux<DtoTeamAvgScorePerMap> getAvgTeamScorePerMap(String mapName, ScoreType scoreType, Optional<Integer> lastMatchesToConsider) {

		String avgFieldName = "avg_score";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT " + Users_scoresDto.Fields.side + "," + Users_scoresDto.Fields.map + ", avg(" + scoreType.toString().toLowerCase() + ") as " + avgFieldName);
		sql.append(" FROM ");
		sql.append(Users_scoresDao.tableName);
		
		sql.append(" WHERE ");
		sql.append(Users_scoresDto.Fields.map + " = :" + Users_scoresDto.Fields.map);
		
		boolean addLimit = lastMatchesToConsider.isPresent() && lastMatchesToConsider.get() > 0;
		if(addLimit) {
			sql.append(" AND ");
			sql.append(Users_scoresDto.Fields.game_date + " IN ");
			
			sql.append(" ( SELECT " + Users_scoresDto.Fields.game_date);
			sql.append(" FROM ");
			sql.append(Users_scoresDao.tableName);
			sql.append(" WHERE ");
			sql.append(Users_scoresDto.Fields.map + " = :" + Users_scoresDto.Fields.map);
			
			sql.append(" GROUP BY ");
			sql.append(Users_scoresDto.Fields.game_date);
			sql.append(" ORDER BY ");
			sql.append(Users_scoresDto.Fields.game_date);
			sql.append(" DESC ");
			sql.append(" LIMIT " + lastMatchesToConsider.get());
			sql.append(" ) ");
		}
		
		sql.append(" group by " + Users_scoresDto.Fields.side + ", " + Users_scoresDto.Fields.map );
		sql.append(" order by " + Users_scoresDto.Fields.map + ", " + Users_scoresDto.Fields.side);
		
		// @formatter:off
		GenericExecuteSpec ges = client.sql(sql.toString())
				.bind(Users_scoresDto.Fields.map, mapName);
		
		return ges.map((Row row, RowMetadata rowMetaData) -> {
			return new DtoTeamAvgScorePerMap(
				row.get(Users_scoresDto.Fields.side, String.class),
				row.get(Users_scoresDto.Fields.map, String.class),
				row.get(avgFieldName, Double.class))
			;
		}).all();
		// @formatter:on
	}
	
	private Flux<DtoUserAvgScorePerMap> getUserAveragaScorePerMaps(String steamId, ScoreType scoreType, Optional<List<String>> maps, Optional<Integer> lastMatchesToConsider) {
		_LOGGER.trace("Inside RepoUserScorePostgres.getUserAveragaScorePerMap");
		String avgFieldName = "average_score";
		StringBuilder sqlFrom = new StringBuilder();
		
		
		
		boolean isUnion = lastMatchesToConsider.isPresent() && lastMatchesToConsider.get() > 0 && maps.isPresent(); 
		if(isUnion) {			
			List<StringBuilder> limits = new ArrayList<>();
			maps.get().forEach(m -> limits.add(prepareQueryForUnionFilterBySteamIdAndMap(scoreType, steamId, m, lastMatchesToConsider.get())));
			sqlFrom.append(" ( " + String.join(" UNION ", limits) + " ) x ");
		}else {			
			sqlFrom.append(Users_scoresDao.tableName);
		}
		
		
		StringBuilder sql = queryMapsNoLimit(sqlFrom, scoreType, steamId, maps.orElse(new ArrayList<>()), avgFieldName);

		// @formatter:off
		GenericExecuteSpec ges = client.sql(sql.toString())
				.bind(Users_scoresDto.Fields.steam_id, steamId);
		
		if(maps.isPresent()) {
			ges = ges.bind(Users_scoresDto.Fields.map, maps.get());
			if (isUnion) {
				for (String map : maps.get()) {
					ges = ges.bind(String.format("%s_%s", steamId, map), map);
				}
			}
		}
		
		
		return ges.map((Row row, RowMetadata rowMetaData) -> {
			return new DtoUserAvgScorePerMap(
					row.get(Users_scoresDto.Fields.steam_id, String.class),
				row.get(Users_scoresDto.Fields.map, String.class),
				row.get(avgFieldName, Double.class))
			;
		}).all();
		// @formatter:on
	}
	
	private StringBuilder queryMapsNoLimit(StringBuilder from, ScoreType scoreType, String steamId, List<String> maps, String avgFieldName) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT " + Users_scoresDto.Fields.map + "," + Users_scoresDto.Fields.steam_id + ", avg(" + scoreType.toString().toLowerCase() + ") as " + avgFieldName);
		sql.append(" FROM ");
		sql.append(from);
		
		sql.append(" WHERE ");
		sql.append(Users_scoresDto.Fields.steam_id);
		sql.append(" =:" + Users_scoresDto.Fields.steam_id);
		
		if(!maps.isEmpty()) {
			sql.append(" AND ");
			sql.append(Users_scoresDto.Fields.map + " IN ");
			sql.append(" (:" + Users_scoresDto.Fields.map + ") ");
		}
		
		sql.append(" group by " + Users_scoresDto.Fields.map + ", " + Users_scoresDto.Fields.steam_id );
		sql.append(" order by " + Users_scoresDto.Fields.map);
		
		return sql;
	}
	
	private StringBuilder prepareQueryForUnionFilterBySteamIdAndMap(ScoreType scoreType, String steamId, String mapName, Integer limit ) {
		StringBuilder innerQuery = new StringBuilder();
		innerQuery.append(" ( SELECT " + Users_scoresDto.Fields.map + "," + Users_scoresDto.Fields.steam_id + ", " + scoreType.toString().toLowerCase());
		innerQuery.append(" FROM ");
		innerQuery.append(Users_scoresDao.tableName);
		innerQuery.append(" WHERE ");
		innerQuery.append(Users_scoresDto.Fields.steam_id);
		innerQuery.append(" =:" + Users_scoresDto.Fields.steam_id);
		
		innerQuery.append(" AND ");
		innerQuery.append(Users_scoresDto.Fields.map + " = ");
		innerQuery.append(String.format(":%s_%s", steamId, mapName));
		
		
		innerQuery.append(" ORDER BY ");
		innerQuery.append(Users_scoresDto.Fields.game_date);
		innerQuery.append(" DESC ");
		innerQuery.append(" LIMIT " + limit);
		innerQuery.append(" ) ");
		
		return innerQuery;
	}
	
	@Override
	public Flux<Users_scoresDto> getUserScoresPerMap(String mapName, Optional<Integer> lastXMatchedToConsider) {
		_LOGGER.trace("Inside RepoUserScorePostgres.getUserScoresPerMap");
		
		boolean addLimit = lastXMatchedToConsider.isPresent() && lastXMatchedToConsider.get() > 0;
		StringBuilder innerQuery = new StringBuilder();
		
		innerQuery.append(" SELECT * FROM ");
		innerQuery.append(Users_scoresDao.tableName);
		innerQuery.append(" WHERE ");
		innerQuery.append(Users_scoresDto.Fields.map);
		innerQuery.append(" =:" + Users_scoresDto.Fields.map);
		
		if(addLimit) {
			innerQuery.append(" AND ");
			innerQuery.append(Users_scoresDto.Fields.game_date);
			innerQuery.append(" IN ( ");
			innerQuery.append(" SELECT ");
			innerQuery.append(Users_scoresDto.Fields.game_date);
			innerQuery.append(" FROM ");
			innerQuery.append(Users_scoresDao.tableName);
			innerQuery.append(" WHERE ");
			innerQuery.append(Users_scoresDto.Fields.map);
			innerQuery.append(" =:" + Users_scoresDto.Fields.map);
			innerQuery.append(" GROUP BY ");
			innerQuery.append(Users_scoresDto.Fields.game_date);
			innerQuery.append(" ORDER BY ");
			innerQuery.append(Users_scoresDto.Fields.game_date);
			innerQuery.append(" DESC ");
			innerQuery.append(" LIMIT ");
			innerQuery.append(lastXMatchedToConsider.get());
			innerQuery.append(" ) ");
		}
		
		innerQuery.append(" ORDER BY ");
		innerQuery.append(Users_scoresDto.Fields.game_date);
		innerQuery.append(" DESC ");
		innerQuery.append("");
		
		Users_scoresDao dao = new Users_scoresDao();
		
		//// @formatter:off
		return client.sql(innerQuery.toString())
				.bind(Users_scoresDto.Fields.map, mapName)
				.map(dao::mappingFunction).all();
		// @formatter:on
	}
}
