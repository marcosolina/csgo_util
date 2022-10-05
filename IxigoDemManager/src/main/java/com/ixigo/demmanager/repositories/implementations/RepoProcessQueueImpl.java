package com.ixigo.demmanager.repositories.implementations;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.models.entities.EntityProcessQueue;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;

public class RepoProcessQueueImpl implements RepoProcessQueue {

	public static final BiFunction<Row, RowMetadata, EntityProcessQueue> MAPPING_FUNCTION = (row, rowMetaData) -> 
	{
		EntityProcessQueue entity = new EntityProcessQueue();
		entity.setFileName(row.get("FILE_NAME", String.class));
		entity.setProcessedOn(row.get("PROCESSED_ON", LocalDateTime.class));
		entity.setProcessStatus(DemProcessStatus.valueOf(row.get("PROCESS_STATUS", String.class)));
		entity.setQueuedOn(row.get("QUEUED_ON", LocalDateTime.class));
		return entity;
	};
	
	
	
	@Autowired
	private DatabaseClient client;
	
	

	@Override
	public Flux<EntityProcessQueue> getNotProcessedDemFiles() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT * FROM DEM_PROCESS_QUEUE");
		query.append(" WHERE ");
		query.append(" PROCESS_STATUS = 'NOT_PROCESSED' ");
		query.append(" ORDER BY");
		query.append(" PROCESSED_ON");

		return client.sql(query.toString()).map(MAPPING_FUNCTION).all();
	}

}
