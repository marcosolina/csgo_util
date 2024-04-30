package com.ixigo.demmanager.repositories.implementations;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
import com.ixigo.library.enums.DateFormats;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.utils.DateUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CrudRepoGeneric implements CrudRepo {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoUserPostgres.class);

	@Autowired
	private DatabaseClient client;

	@Override
	public <T extends IxigoDto, D extends IxigoDao<T>> Flux<T> getAll(Class<D> daoClass,
			Optional<Map<String, String>> whereClause) {
		_LOGGER.trace("Inside CrudRepoGeneric.getAll");
		try {
			D dao = daoClass.getConstructor().newInstance();

			if (whereClause.isPresent() && !whereClause.isEmpty()) {
				whereClause.get().forEach((k, v) -> {
					dao.addSqlWhereAndClauses(k);
					if (!"steamid".equals(k)) {
						dao.addSqlParams(tryToConvertToSpecificType(v));
					} else {
						dao.addSqlParams(v);
					}
				});
			}

			return dao.prepareSqlSelect(client).map(dao::mappingFunction).all();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

	private Object tryToConvertToSpecificType(String obj) {
		try {
			return DateUtils.fromStringToLocalDateTime(obj, DateFormats.DB_TIME_STAMP);
		} catch (Exception e) {
			try {
				return new BigDecimal(obj);
			} catch (Exception e1) {
			}
		}
		return obj;
	}

	@Override
	public <T extends IxigoDto, D extends IxigoDao<T>> Mono<Boolean> insert(Class<D> daoClass, T dto) {
		_LOGGER.trace("Inside CrudRepoGeneric.getAll");
		try {
			D dao = daoClass.getConstructor().newInstance();
			dao.setDto(dto);
			return dao.prepareSqlInsert(client).then().thenReturn(true);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

	@Override
	public <T extends IxigoDto, D extends IxigoDao<T>> Mono<Boolean> delete(Class<D> daoClass, T dto) {
		_LOGGER.trace("Inside CrudRepoGeneric.delete");

		try {
			D dao = daoClass.getConstructor().newInstance();
			dao.setDto(dto);

			return dao.prepareSqlSelectByKey(client).then().thenReturn(true);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

	@SuppressWarnings("all")
	@Override
	public Flux<IxigoDto> getAll(String tableName, Optional<Map<String, String>> whereClause) {
		_LOGGER.trace("Inside CrudRepoGeneric.getAll");
		try {
			String daoName = tableName.toUpperCase().charAt(0) + tableName.substring(1) + "Dao";
			Class c = Class.forName("com.ixigo.demmanager.models.database." + daoName);
			IxigoDao dao = (IxigoDao) c.getConstructor().newInstance();
			return this.getAll(dao.getClass(), whereClause);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
		}
	}

	@Override
	public <T extends IxigoDto, D extends IxigoDao<T>> Mono<T> insertAndSelect(Class<D> daoClass, T dto,
			List<String> sqlWhereAndClauses, List<Object> sqlParams) {
		return this.insert(daoClass, dto).flatMap(status -> {
			try {
				D dao = daoClass.getConstructor().newInstance();
				sqlWhereAndClauses.forEach(dao::addSqlWhereAndClauses);
				sqlParams.forEach(dao::addSqlParams);
				return dao.prepareSqlSelect(client).map(dao::mappingFunction).one();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
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

		// @formatter:off
		return dao.prepareSqlSelect(client)
				.map(dao::mappingFunction)
				.all()
				.filter(dto -> !dto.getRoutine_name().startsWith("truncate_tables"))
				.flatMap(dto -> {
					String sql = String.format("select %s()", dto.getRoutine_name());
					_LOGGER.debug(String.format("Executing: %s", sql));
					return client.sql(sql).fetch().all();
				})
				.collectList()
				.map(list -> true);
		// @formatter:on
	}
}
