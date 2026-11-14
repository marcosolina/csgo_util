package com.ixigo.demmanager.services.implementations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.mappers.SvcMapper;
import com.ixigo.demmanager.repositories.interfaces.CrudRepo;
import com.ixigo.demmanager.services.interfaces.ChartsData;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;

/**
 * My implementation of the @ChartsData
 * 
 * @author marco
 *
 */
public class ChartsDataImp implements ChartsData {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ChartsDataImp.class);
	@Autowired
	private CrudRepo genericRepo;
	@Autowired
	private SvcMapper mapper;
	
	@Override
	public Flux<?> getDataForTable(String tableName, Optional<Map<String, String>> whereClause) {
		_LOGGER.trace("Inside ChartsDataImp.getDataForTable");
		try {
			String dtoName = tableName.toUpperCase().charAt(0) + tableName.substring(1) + "Dto";
			Class<?> c = Class.forName("com.ixigo.demmanager.models.database." + dtoName);
			Method m = mapper.getClass().getMethod("fromDtoToSvc", c);
			return genericRepo.getAll(tableName, whereClause).map(dto -> {
				try {
					return m.invoke(mapper, dto);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
				}
			});
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e1.getMessage(), ErrorCodes.GENERIC);
		}
	}

}
