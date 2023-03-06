package com.ixigo.rconapi.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.rconapi.commands.PostRconCmd;
import com.ixigo.rconapi.mappers.RestMapper;
import com.ixigo.rconapi.models.rest.RestRconResponse;
import com.ixigo.rconapi.services.interfaces.RconService;

import reactor.core.publisher.Mono;

@Component
public class PostRconCmdHandler implements WebCommandHandler<PostRconCmd, RestRconResponse> {

	@Autowired
	private RconService service;
	@Autowired
    private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestRconResponse>> handle(PostRconCmd cmd) {

		var req = cmd.getRequest();

		// @formatter:off
		return service.sendRconCommand(
				req.getRconHost(),
				req.getRconPort(),
				req.getRconPass(),
				req.getRconCmd())
			.map(stringResp -> {
				var resp = new RestRconResponse();
				resp.setRconResponse(stringResp);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				})
			.defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
			;
		// @formatter:on
	}
}
