package com.ixigo.demmanager.handlers.charts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.charts.CmdGetTeamsScorePerMap;
import com.ixigo.demmanager.mappers.RestMapper;
import com.ixigo.demmanager.services.interfaces.ChartsData;
import com.ixigo.demmanagercontract.models.rest.charts.RestTeamScorePerMap;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUserGotvScore;
import com.ixigo.library.enums.DateFormats;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.library.utils.DateUtils;
import com.ixigo.library.validators.IxigoValidator;
import com.ixigo.library.validators.IxigoValidatorUtils;
import com.ixigo.library.validators.ValidationException;

import br.com.fluentvalidator.context.ValidationResult;
import reactor.core.publisher.Mono;

/**
 * Handler used to retrieve the teams score per map.
 * @author marco
 *
 */
@Component
public class CmdGetTeamsScorePerMapHandler implements WebCommandHandler<CmdGetTeamsScorePerMap, RestTeamScorePerMap> {
	@Autowired
	private ChartsData chartsService;
	@Autowired
	private RestMapper mapper;
	
	@Autowired
    private IxigoValidator<CmdGetTeamsScorePerMap> validator;
	
	@Override
	public Mono<ResponseEntity<RestTeamScorePerMap>> handle(CmdGetTeamsScorePerMap cmd) {
		
		ValidationResult result = validator.validate(cmd);

        if (!result.isValid()) {
        	return Mono.error(new ValidationException(IxigoValidatorUtils.fromCollectionErrorToValidationError(result.getErrors())));
        }
		
        Optional<Integer> lastMatchesToConsider = cmd.getMatchesToConsider() != null ? Optional.of(Integer.parseInt(cmd.getMatchesToConsider())): Optional.empty();
        
		// @formatter:off
		return chartsService.getTeamsScorePerMap(cmd.getMap(), lastMatchesToConsider)
			.map(map -> {
				RestTeamScorePerMap resp = new RestTeamScorePerMap();
				Map<String, List<RestUserGotvScore>> matches = new HashMap<>();
				resp.setMatches(matches);
				
				map.forEach((k,v) ->{
					matches.put(
						DateUtils.fromLocalDateTimeToString(v.getPlayedOn(), DateFormats.DB_DATE),
						mapper.fromSvcToRestGoTvList(v.getUsersStats()));
				});
				
				return resp;
			}).map(resp -> new ResponseEntity<>(resp, HttpStatus.OK));
		// @formatter:on
	}

}
