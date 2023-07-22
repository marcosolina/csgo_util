package com.ixigo.demmanager.handlers.charts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.charts.CmdGetViewData;
import com.ixigo.demmanager.repositories.interfaces.CrudRepo;
import com.ixigo.demmanagercontract.models.rest.charts.RestViewData;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class GetGetViewDataHandler implements WebCommandHandler<CmdGetViewData, RestViewData> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetGetViewDataHandler.class);
	@Autowired
	private CrudRepo genericRepo;
	
	@Override
	public Mono<ResponseEntity<RestViewData>> handle(CmdGetViewData cmd) {
		_LOGGER.trace("Inside GetGetViewDataHandler.handle");
		String tableName = cmd.getViewName().toLowerCase();
		String daoName = tableName.toUpperCase().charAt(0) + tableName.substring(1) + "Dao";
		
		Optional<List<String>> whereClause = Optional.empty();
		Optional<List<String>> whereValues = Optional.empty();
		if (cmd.getAllRequestParams() != null && !cmd.getAllRequestParams().isEmpty()) {
			List<String> wc = new ArrayList<>();
			List<String> wv = new ArrayList<>();
			cmd.getAllRequestParams().forEach((k,v) -> {
				wc.add(k);
				wv.add(v);
			});
			whereClause = Optional.of(wc);
			whereValues = Optional.of(wv);
		}
		
		return genericRepo.getAll(daoName, whereClause, whereValues).collectList().map(list -> {
			RestViewData resp = new RestViewData();
			resp.setData(list);
			resp.setViewName(cmd.getViewName());
			return resp;
		}).map(resp -> new ResponseEntity<>(resp, HttpStatus.OK));
	}

}
