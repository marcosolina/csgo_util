package com.ixigo.demmanager.repositories.implementations;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.models.entities.DemProcessQueueDao;
import com.ixigo.demmanager.models.entities.DemProcessQueueDto;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;

/**
 * Repo implementation used for Postgres DB
 * @author Marco
 *
 */
public class RepoProcessQueuePostgres implements RepoProcessQueue {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoProcessQueuePostgres.class);

	public static final BiFunction<Row, RowMetadata, DemProcessQueueDto> MAPPING_FUNCTION = (row, rowMetaData) -> {
		DemProcessQueueDto entity = new DemProcessQueueDto();
		entity.setFileName(row.get("FILE_NAME", String.class));
		entity.setProcessedOn(row.get("PROCESSED_ON", LocalDateTime.class));
		entity.setProcessStatus(DemProcessStatus.valueOf(row.get("PROCESS_STATUS", String.class)));
		entity.setQueuedOn(row.get("QUEUED_ON", LocalDateTime.class));
		return entity;
	};

	@Autowired
	private DatabaseClient client;

	@Override
	public Flux<DemProcessQueueDto> getNotProcessedDemFiles() {
		_LOGGER.trace("Inside RepoProcessQueueImpl.getNotProcessedDemFiles");
		DemProcessQueueDao dao = new DemProcessQueueDao();
		dao.addSqlWhereAndClauses("PROCESS_STATUS");
		

		// @formatter:off
		return client.sql(dao.getSqlSelect().toString())
				.bind("PROCESS_STATUS", "NOT_PROCESSED")
				.map(MAPPING_FUNCTION).all();
		// @formatter:on
	}

}
