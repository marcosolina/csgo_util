package com.ixigo.demmanager.repositories.implementations;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.models.entities.Dem_process_queueDao;
import com.ixigo.demmanager.models.entities.Dem_process_queueDto;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.ixigo.library.errors.IxigoException;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repo implementation used for Postgres DB
 * 
 * @author Marco
 *
 */
public class RepoProcessQueuePostgres implements RepoProcessQueue {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoProcessQueuePostgres.class);

	public static final BiFunction<Row, RowMetadata, Dem_process_queueDto> MAPPING_FUNCTION = (row, rowMetaData) -> {
		Dem_process_queueDto entity = new Dem_process_queueDto();
		entity.setFile_name(row.get("FILE_NAME", String.class));
		entity.setProcessed_on(row.get("PROCESSED_ON", LocalDateTime.class));
		entity.setProcess_status(DemProcessStatus.valueOf(row.get("PROCESS_STATUS", String.class)));
		entity.setQueued_on(row.get("QUEUED_ON", LocalDateTime.class));
		return entity;
	};

	@Autowired
	private DatabaseClient client;

	@Override
	public Flux<Dem_process_queueDto> getNotProcessedDemFiles() {
		_LOGGER.trace("Inside RepoProcessQueueImpl.getNotProcessedDemFiles");
		Dem_process_queueDao dao = new Dem_process_queueDao();
		dao.addSqlWhereAndClauses("PROCESS_STATUS");

		// @formatter:off
		return client.sql(dao.getSqlSelect().toString())
				.bind("PROCESS_STATUS", "NOT_PROCESSED")
				.map(MAPPING_FUNCTION).all();
		// @formatter:on
	}

	@Override
	public Mono<Void> saveEntity(Dem_process_queueDto entity) {
		Dem_process_queueDao dao = new Dem_process_queueDao();
		dao.setDto(entity);

		return findById(entity.getFile_name()).switchIfEmpty(Mono.just(new Dem_process_queueDto())).flatMap(dto -> {
			if (!"".equals(dto.getFile_name())) {
				throw new IxigoException(HttpStatus.CONFLICT, "Duplicated");
			}
			return dao.prepareInsert(client).then();
		});

	}

	@Override
	public Mono<Dem_process_queueDto> findById(String absoluteFileName) {
		Dem_process_queueDao dao = new Dem_process_queueDao();
		dao.addSqlWhereAndClauses("file_name");

		// @formatter:off
		return client.sql(dao.getSqlSelect().toString())
				.bind("file_name", absoluteFileName)
				.map(MAPPING_FUNCTION).one();
		// @formatter:on
	}

}
