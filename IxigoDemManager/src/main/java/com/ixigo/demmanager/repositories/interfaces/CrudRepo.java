package com.ixigo.demmanager.repositories.interfaces;

import com.ixigo.library.dao.IxigoDao;
import com.ixigo.library.dto.IxigoDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudRepo {
	public <T extends IxigoDto, D extends IxigoDao<T>> Flux<T> getAll(Class<D> daoClass);
	public Flux<IxigoDto> getAll(String daoName);
	public <T extends IxigoDto, D extends IxigoDao<T>> Mono<Boolean> insert(Class<D> daoClass, T dto);
}