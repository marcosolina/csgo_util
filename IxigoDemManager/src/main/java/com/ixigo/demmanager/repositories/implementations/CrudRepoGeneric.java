package com.ixigo.demmanager.repositories.implementations;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.repositories.interfaces.CrudRepo;
import com.ixigo.library.dao.IxigoDao;
import com.ixigo.library.dto.IxigoDto;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CrudRepoGeneric implements CrudRepo {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoUserPostgres.class);

	@Autowired
	private DatabaseClient client;

	@Override
	public <T extends IxigoDto, D extends IxigoDao<T>> Flux<T> getAll(Class<D> daoClass, Class<T> dtoClass) {
		_LOGGER.trace("Inside CrudRepoGeneric.getAll");
		try {
			D dao = daoClass.getConstructor().newInstance();
			return dao.prepareSqlSelect(client).map(dao::mappingFunction).all();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

	@Override
	public <T extends IxigoDto, D extends IxigoDao<T>> Mono<Boolean> insert(Class<D> daoClass, Class<T> dtoClass, T dto) {
		_LOGGER.trace("Inside CrudRepoGeneric.getAll");
		try {
			D dao = daoClass.getConstructor().newInstance();
			dao.setDto(dto);
			return dao.prepareSqlInsert(client).then().thenReturn(true);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

	
	

}
