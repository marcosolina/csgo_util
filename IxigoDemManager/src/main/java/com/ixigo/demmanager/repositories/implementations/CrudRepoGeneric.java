package com.ixigo.demmanager.repositories.implementations;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.models.database.FunctionsDao;
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
	public <T extends IxigoDto, D extends IxigoDao<T>> Flux<T> getAll(Class<D> daoClass, Optional<List<String>> whereClause, Optional<List<String>> whereValues) {
		_LOGGER.trace("Inside CrudRepoGeneric.getAll");
		try {
			D dao = daoClass.getConstructor().newInstance();

			if (whereClause.isPresent() && whereClause.isPresent()) {
				whereClause.get().forEach(dao::addSqlWhereAndClauses);
				whereValues.get().forEach(v -> {
					try {
						BigDecimal number = new BigDecimal(v);
						dao.addSqlParams(number);
					} catch (Exception e) {
						dao.addSqlParams(v);
					}
				});
			}

			return dao.prepareSqlSelect(client).map(dao::mappingFunction).all();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

	@Override
	public <T extends IxigoDto, D extends IxigoDao<T>> Mono<Boolean> insert(Class<D> daoClass, T dto) {
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

	@SuppressWarnings("all")
	@Override
	public Flux<IxigoDto> getAll(String daoName, Optional<List<String>> whereClause, Optional<List<String>> whereValues) {
		_LOGGER.trace("Inside CrudRepoGeneric.getAll");
		try {
			Class c = Class.forName("com.ixigo.demmanager.models.database." + daoName);
			IxigoDao dao = (IxigoDao) c.getConstructor().newInstance();
			return this.getAll(dao.getClass(), whereClause, whereValues);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

	@Override
	public <T extends IxigoDto, D extends IxigoDao<T>> Mono<T> insertAndSelect(Class<D> daoClass, T dto, List<String> sqlWhereAndClauses, List<Object> sqlParams) {
		return this.insert(daoClass, dto).flatMap(status -> {
			try {
				D dao = daoClass.getConstructor().newInstance();
				sqlWhereAndClauses.forEach(dao::addSqlWhereAndClauses);
				sqlParams.forEach(dao::addSqlParams);
				return dao.prepareSqlSelect(client).map(dao::mappingFunction).one();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				_LOGGER.error(e.getMessage());
				throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
			}
		});
	}

	@Override
	public Mono<Boolean> triggerAllFunctions() {
		FunctionsDao dao = new FunctionsDao();
		dao.addSqlWhereAndClauses("routine_type");
		dao.addSqlWhereAndClauses("routine_schema");
		dao.addSqlParams("FUNCTION");
		dao.addSqlParams("public");

		return dao.prepareSqlSelect(client).map(dao::mappingFunction).all().flatMap(dto -> {
			String sql = String.format("select %s()", dto.getRoutine_name());
			_LOGGER.debug("Executing: %s", sql);
			return client.sql(sql).fetch().all();
		}).map(map -> {
			return map;
		}).collectList().map(list -> true);
	}
}
