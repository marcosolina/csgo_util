package com.ixigo.demmanager.repositories.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.DatabaseClient.GenericExecuteSpec;

import com.ixigo.demmanager.models.database.DtoMapPlayedCounter;
import com.ixigo.demmanager.models.database.Users_scoresDao;
import com.ixigo.demmanager.models.database.Users_scoresDto;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
		sql.append(" order by " + Users_scoresDto.Fields.map);
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
	public Flux<Users_scoresDto> getLastXUserScores(Integer counter, String steamID, BigDecimal minPercPLayer) {
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
			.bind(Users_scoresDto.Fields.mp, minPercPLayer)
			.bind("limit", counter)
			.map(dao::mappingFunction)
			.all();
		// @formatter:on
	}

}
