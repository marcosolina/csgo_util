package com.ixigo.discordbot.handlers.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.errors.IxigoErrorCmd;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class IxigoErrorCmdHandler implements WebCommandHandler<IxigoErrorCmd, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoErrorCmdHandler.class);
	@Autowired
	private IxigoBot botService;

	@Override
	public Mono<ResponseEntity<Void>> handle(IxigoErrorCmd cmd) {
		_LOGGER.trace("Inside IxigoErrorCmdHandler.handle");
		botService.sendMessageToGeneralChat(cmd.getErrorMsg());
		return Mono.just(new ResponseEntity<Void>(HttpStatus.ACCEPTED));
	}

}
