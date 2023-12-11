package com.ixigo.discordbot.handlers.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.users.MoveToGeneralVoiceChannelCmd;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class MoveToGeneralVoiceChannelCmdHandler implements WebCommandHandler<MoveToGeneralVoiceChannelCmd, Boolean> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(MoveToGeneralVoiceChannelCmdHandler.class);
	@Autowired
	private IxigoBot botService;
	@Override
	public Mono<ResponseEntity<Boolean>> handle(MoveToGeneralVoiceChannelCmd cmd) {
		_LOGGER.trace("Inside MoveToGeneralVoiceChannelCmdHandler.handle");
		botService.moveAllMembersIntoGeneralChannel();
		return Mono.just(new ResponseEntity<>(true, HttpStatus.OK));
	}

}
