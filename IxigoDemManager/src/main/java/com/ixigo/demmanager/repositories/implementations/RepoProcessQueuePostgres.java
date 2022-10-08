package com.ixigo.demmanager.repositories.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.models.entities.Dem_process_queueDao;
import com.ixigo.demmanager.models.entities.Dem_process_queueDto;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;

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

	@Autowired
	private DatabaseClient client;
	@Autowired
	private IxigoMessageResource msgSource;

	@Override
	public Flux<Dem_process_queueDto> getNotProcessedDemFiles() {
		_LOGGER.trace("Inside RepoProcessQueueImpl.getNotProcessedDemFiles");
		Dem_process_queueDao dao = new Dem_process_queueDao();
		dao.addSqlWhereAndClauses("PROCESS_STATUS");
		dao.addSqlParams(DemProcessStatus.NOT_PROCESSED);

		return dao.prepareSqlSelect(client).map(dao::mappingFunction).all();
	}

	@Override
	public Mono<Boolean> saveDto(Dem_process_queueDto entity) {
		Dem_process_queueDao dao = new Dem_process_queueDao();
		dao.setDto(entity);

		// @formatter:off
		return findById(entity.getFile_name())
			.switchIfEmpty(Mono.just(new Dem_process_queueDto()))
			.flatMap(dto -> {
				if (!"".equals(dto.getFile_name())) {
					throw new IxigoException(HttpStatus.CONFLICT, msgSource.getMessage(ErrorCodes.DUPLICATE_VALUE));
				}
				return dao.prepareSqlInsert(client).then().thenReturn(true);
			});
		// @formatter:on

	}

	@Override
	public Mono<Dem_process_queueDto> findById(String absoluteFileName) {
		Dem_process_queueDao dao = new Dem_process_queueDao();
		dao.addSqlWhereAndClauses("file_name");
		dao.addSqlParams(absoluteFileName);

		return dao.prepareSqlSelect(client).map(dao::mappingFunction).one();
	}

}
