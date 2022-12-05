package com.ixigo.discordbot.repositories.implementations;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.discordbot.models.repo.Users_mapDao;
import com.ixigo.discordbot.models.repo.Users_mapDto;
import com.ixigo.discordbot.repositories.interfaces.RepoUsersMap;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RepoUsersMapPostgres implements RepoUsersMap {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoUsersMapPostgres.class);

	@Autowired
	private DatabaseClient client;

	@Override
	public Mono<Boolean> insertOrUpdate(Users_mapDto entity) {
		_LOGGER.trace("Inside RepoUsersMapPostgres.insertOrUpdate");
		Users_mapDao dao = new Users_mapDao();
		dao.setDto(entity);

		// @formatter:off
		return findById(entity.getDiscord_id())
			.switchIfEmpty(Mono.just(new Users_mapDto()))
			.flatMap(dto -> {
				// Update
				if (dto.getDiscord_id() != 0) {
					return dao.prepareSqlUpdate(client).then().thenReturn(true);
				}
				return dao.prepareSqlInsert(client).then().thenReturn(true);
			});
		// @formatter:on
	}

	@Override
	public Mono<Users_mapDto> findById(Long discordId) {
		_LOGGER.trace("Inside RepoUsersMapPostgres.findById");

		Users_mapDao dao = new Users_mapDao();
		dao.setDiscord_id(discordId);

		return dao.prepareSqlSelectByKey(client).map(dao::mappingFunction).one();
	}

	@Override
	public Flux<Users_mapDto> getAll() {
		_LOGGER.trace("Inside RepoUsersMapPostgres.getAll");
		Users_mapDao dao = new Users_mapDao();
		return dao.prepareSqlSelect(client).map(dao::mappingFunction).all();
	}

	@Override
	public Flux<Users_mapDto> findAllById(List<Long> discordIds) {
		_LOGGER.trace("Inside RepoUsersMapPostgres.findAllById");
		
		if(discordIds == null || discordIds.isEmpty()) {
			return Flux.empty();
		}
		
		Users_mapDao dao = new Users_mapDao();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM " + Users_mapDao.tableName);
		sql.append(" WHERE " + Users_mapDto.Fields.discord_id);
		sql.append(" IN (:ids) ");
		sql.append(" AND "  + Users_mapDto.Fields.steam_id + " IS NOT NULL");
		
		// @formatter:off
		return client.sql(sql.toString())
			.bind("ids", discordIds)
			.map(dao::mappingFunction)
			.all();
		// @formatter:on
	}

}
