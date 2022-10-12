package com.ixigo.demmanager.repositories.interfaces;

import com.ixigo.demmanager.models.entities.Dem_process_queueDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepoProcessQueue {
	/**
	 * It returns a list of DEM files that are not processed yet
	 * 
	 * @return
	 */
	public Flux<Dem_process_queueDto> getNotProcessedDemFiles();

	/**
	 * It saves the DTO into the DB
	 * 
	 * @param entity
	 * @return
	 */
	public Mono<Boolean> insertOrUpdate(Dem_process_queueDto dto);

	/**
	 * It returns the entity associated to the ID
	 * 
	 * @param absoluteFileName
	 * @return
	 */
	public Mono<Dem_process_queueDto> findById(String absoluteFileName);
}
