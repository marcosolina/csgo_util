package com.ixigo.discordbot.handlers.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.users.SetToVoiceChannelCmd;
import com.ixigo.discordbot.mappers.RestMapper;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class SetToVoiceChannelCmdHandler implements WebCommandHandler<SetToVoiceChannelCmd, Boolean> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(SetToVoiceChannelCmdHandler.class);
	@Autowired
	private IxigoBot botService;
	@Autowired
	private RestMapper mapper;
	@Override
	public Mono<ResponseEntity<Boolean>> handle(SetToVoiceChannelCmd cmd) {
		_LOGGER.trace("Inside SetToVoiceChannelCmdHandler.handle");
		
		// @formatter:off
		return botService.setToVoiceChannel(mapper.fromRestToSvc(cmd.getTeams()))
			.thenReturn(new ResponseEntity<>(true, HttpStatus.OK))
			;
		// @formatter:on
	}

}
