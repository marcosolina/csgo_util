package com.ixigo.rconapi.services.implementations;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.GameServer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.validators.IxigoValidator;
import com.ixigo.library.validators.IxigoValidatorUtils;
import com.ixigo.library.validators.ValidationException;
import com.ixigo.rconapi.models.service.SvcRconRequest;
import com.ixigo.rconapi.services.interfaces.RconService;

import br.com.fluentvalidator.context.ValidationResult;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class RconServiceSteamCondenser implements RconService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RconServiceSteamCondenser.class);
	
	@Autowired
    private IxigoValidator<SvcRconRequest> validator;

	@Override
	public Mono<String> sendRconCommand(SvcRconRequest request) throws IxigoException {
		
		ValidationResult result = validator.validate(request);

        if (!result.isValid()) {
        	return Mono.error(new ValidationException(IxigoValidatorUtils.fromCollectionErrorToValidationError(result.getErrors())));
        }
		
		return Mono.fromSupplier(() -> {
			try {
				GameServer server = new SourceServer(request.getRconHost(), request.getRconPort());
				LOGGER.debug(String.format("Authenticating on server: %s with password: %s", request.getRconHost(), request.getRconPass()));
				server.rconAuth(request.getRconPass());
				LOGGER.debug(String.format("Sending RCON command: %s", request.getRconCmd()));
				String response = server.rconExec(request.getRconCmd());
				LOGGER.debug(String.format("RCON response: %s", response));
				return response;
			} catch (SteamCondenserException | TimeoutException e) {
				throw new IxigoException(HttpStatus.BAD_GATEWAY, e.getMessage(), "RCON00005");
			}
		}).subscribeOn(Schedulers.boundedElastic());
	}

}
