package com.ixigo.demmanager.handlers.charts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.charts.CmdGetAvgScorePerMap;
import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.demmanager.mappers.RestMapper;
import com.ixigo.demmanager.services.interfaces.ChartsData;
import com.ixigo.demmanagercontract.models.rest.charts.RestAvgScorePerMap;
import com.ixigo.demmanagercontract.models.rest.charts.RestAvgScoresPerMap;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.library.validators.IxigoValidator;
import com.ixigo.library.validators.IxigoValidatorUtils;
import com.ixigo.library.validators.ValidationException;

import br.com.fluentvalidator.context.ValidationResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Handler used to manage request to retrieve the average score of the users per map
 * 
 * @author marco
 *
 */
@Component
public class GetAvgScorePerMapHandler implements WebCommandHandler<CmdGetAvgScorePerMap, RestAvgScoresPerMap> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetAvgScorePerMapHandler.class);
	@Autowired
	private ChartsData service;
	@Autowired
	private RestMapper mapper;
	
	@Autowired
    private IxigoValidator<CmdGetAvgScorePerMap> validator;

	@Override
	public Mono<ResponseEntity<RestAvgScoresPerMap>> handle(CmdGetAvgScorePerMap request) {
		_LOGGER.trace("Inside GetAvgScorePerMapHandler.handle");

		ValidationResult result = validator.validate(request);

        if (!result.isValid()) {
        	return Mono.error(new ValidationException(IxigoValidatorUtils.fromCollectionErrorToValidationError(result.getErrors())));
        }
		
        var scoreType = ScoreType.valueOf(request.getScoreType());
        
        Optional<List<String>> maps = request.getMaps() != null && !request.getMaps().isEmpty() ? Optional.ofNullable(request.getMaps()) : Optional.empty();
        Optional<Integer> lastMatchesToConsider = request.getMatchesToConsider() != null ? Optional.of(Integer.parseInt(request.getMatchesToConsider())): Optional.empty();
        
        // @formatter:off
        return Flux.fromIterable(request.getSteamIds())
	        .flatMap(steamId -> service.getUserAverageScorePerMap(steamId, scoreType, maps, lastMatchesToConsider).collectList())
	        .collectList()
	        .filter(l -> l != null && !l.isEmpty())
	        .map(list -> {
	        	Map<String, List<RestAvgScorePerMap>> map = new HashMap<>();
	        	list.forEach(l -> {
	        		if(l != null && !l.isEmpty()) {
	        			map.put(l.get(0).getSteamId(), mapper.fromSvcToRestAvgScorePerMapList(l));
	        		}
	        	});
	        	return map;
	        })
	        .map(m -> {
	        	RestAvgScoresPerMap resp = new RestAvgScoresPerMap();
				resp.setScores(m);
				return new ResponseEntity<>(resp, HttpStatus.OK);
	        })
	        ;
		// @formatter:on
	}

}
