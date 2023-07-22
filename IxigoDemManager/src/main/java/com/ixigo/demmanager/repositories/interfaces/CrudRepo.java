package com.ixigo.demmanager.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import com.ixigo.library.dao.IxigoDao;
import com.ixigo.library.dto.IxigoDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudRepo {
	public <T extends IxigoDto, D extends IxigoDao<T>> Flux<T> getAll(Class<D> daoClass, Optional<List<String>> whereClause, Optional<List<String>> whereValues);
	public Flux<IxigoDto> getAll(String daoName, Optional<List<String>> whereClause, Optional<List<String>> whereValues);
	public <T extends IxigoDto, D extends IxigoDao<T>> Mono<Boolean> insert(Class<D> daoClass, T dto);
	public <T extends IxigoDto, D extends IxigoDao<T>> Mono<T> insertAndSelect(Class<D> daoClass, T dto, List<String> sqlWhereAndClausesm, List<Object> sqlParams);
	
	public Mono<Boolean> triggerAllFunctions();
}
