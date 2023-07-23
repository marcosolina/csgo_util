package com.ixigo.demmanager.handlers.charts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.charts.CmdGetViewData;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.mappers.RestMapper;
import com.ixigo.demmanager.services.interfaces.ChartsData;
import com.ixigo.demmanagercontract.models.rest.charts.RestViewData;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class GetGetViewDataHandler implements WebCommandHandler<CmdGetViewData, RestViewData> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetGetViewDataHandler.class);
	
	@Autowired
	private ChartsData service;
	@Autowired
	private RestMapper mapper;
	
	@Override
	public Mono<ResponseEntity<RestViewData>> handle(CmdGetViewData cmd) {
		_LOGGER.trace("Inside GetGetViewDataHandler.handle");
		String tableName = cmd.getViewName().toLowerCase();
		
		return service.getDataForTable(tableName, Optional.ofNullable(cmd.getAllRequestParams()))
			.map(svc -> {
				Method m;
				try {
					m = mapper.getClass().getMethod("fromSvcToRest", svc.getClass());
					return m.invoke(mapper, svc);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
				}
			})
			.collectList()
			.map(list -> {
				RestViewData resp = new RestViewData();
				resp.setData(list);
				resp.setViewName(cmd.getViewName());
				return resp;
			})
			.map(resp -> new ResponseEntity<>(resp, HttpStatus.OK));
		
		
		/*
		return genericRepo.getAll(daoName, Optional.ofNullable(cmd.getAllRequestParams()))
				.collectList()
				.map(list -> {
					RestViewData resp = new RestViewData();
					resp.setData(list);
					resp.setViewName(cmd.getViewName());
					return resp;
				})
				.map(resp -> new ResponseEntity<>(resp, HttpStatus.OK));
				*/
	}

}
