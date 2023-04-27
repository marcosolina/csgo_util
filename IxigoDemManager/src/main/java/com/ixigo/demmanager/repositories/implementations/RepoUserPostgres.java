package com.ixigo.demmanager.repositories.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.models.database.UsersDao;
import com.ixigo.demmanager.models.database.UsersDto;
import com.ixigo.demmanager.repositories.interfaces.RepoUser;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Postgres implementation for @RepoUser
 * 
 * @author Marco
 *
 */
public class RepoUserPostgres implements RepoUser {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoUserPostgres.class);

	@Autowired
	private DatabaseClient client;

	@Override
	public Flux<UsersDto> getUsers() {
		_LOGGER.trace("Inside RepoUserPostgres.getUsers");
		UsersDao dao = new UsersDao();
		return dao.prepareSqlSelect(client).map(dao::mappingFunction).all();
	}

	@Override
	public synchronized Mono<Boolean> insertUpdateUser(UsersDto user) {
		_LOGGER.trace("Inside RepoUserPostgres.insertUpdateUser");
		
		UsersDao dao = new UsersDao();
		dao.setDto(user);

		// @formatter:off
		return findById(user.getSteam_id())
			.switchIfEmpty(Mono.just(new UsersDto()))
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
	public synchronized Mono<UsersDto> findById(String steamID) {
		_LOGGER.trace("Inside RepoUserPostgres.findById");

		UsersDao dao = new UsersDao();
		dao.addSqlWhereAndClauses(UsersDto.Fields.steam_id);
		dao.addSqlParams(steamID);

		return dao.prepareSqlSelect(client).map(dao::mappingFunction).one();
	}

}
