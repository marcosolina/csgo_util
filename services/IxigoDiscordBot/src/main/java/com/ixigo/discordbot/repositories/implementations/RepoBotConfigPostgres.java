package com.ixigo.discordbot.repositories.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.discordbot.models.repo.Bot_configDao;
import com.ixigo.discordbot.models.repo.Bot_configDto;
import com.ixigo.discordbot.repositories.interfaces.RepoBotConfig;
import com.ixigo.enums.BotConfigKey;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RepoBotConfigPostgres implements RepoBotConfig {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoBotConfigPostgres.class);

	@Autowired
	private DatabaseClient client;

	@Override
	public Mono<Bot_configDto> fingConfig(BotConfigKey key) {
		_LOGGER.trace("Inside RepoBotConfigPostgres.fingConfig");

		Bot_configDao dao = new Bot_configDao();
		dao.setConfig_key(key);

		return dao.prepareSqlSelectByKey(client).map(dao::mappingFunction).one();
	}

	@Override
	public Mono<Boolean> insertOtUpdate(Bot_configDto dto) {
		_LOGGER.trace("Inside RepoBotConfigPostgres.insertOtUpdate");
		Bot_configDao dao = new Bot_configDao();
		dao.setDto(dto);

		// @formatter:off
		return fingConfig(dto.getConfig_key())
			.switchIfEmpty(Mono.just(new Bot_configDto()))
			.flatMap(config -> {
				// Update
				if (config.getConfig_key() != null) {
					return dao.prepareSqlUpdate(client).then().thenReturn(true);
				}
				return dao.prepareSqlInsert(client).then().thenReturn(true);
			});
		// @formatter:on
	}

	@Override
	public Flux<Bot_configDto> findAllConfig() {
		_LOGGER.trace("Inside RepoBotConfigPostgres.fingAllConfig");

		Bot_configDao dao = new Bot_configDao();
		dao.addSqlOrderFields(Bot_configDto.Fields.config_value_type);
		dao.addSqlOrderFields(Bot_configDto.Fields.config_key);
		return dao.prepareSqlSelect(client).map(dao::mappingFunction).all();
	}

}
