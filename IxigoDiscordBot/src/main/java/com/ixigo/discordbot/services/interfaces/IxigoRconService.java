package com.ixigo.discordbot.services.interfaces;

import java.util.List;
import java.util.Map;

import com.ixigo.discordbot.enums.TeamType;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.models.rest.RestUser;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;
import com.ixigo.serverhelper.models.rest.Cs2InputModel;

import reactor.core.publisher.Mono;

public interface IxigoRconService {
	public Mono<Map<TeamType, List<RestUser>>> getCurrentActivePlayersOnTheIxiGoServer() throws IxigoException;
    public Mono<Boolean> kickTheBots() throws IxigoException;
    public Mono<Boolean> restartIxiGoMatch() throws IxigoException;
    public Mono<Boolean> movePlayersToAppropriateTeam(RestTeams teams) throws IxigoException;
    public Mono<Boolean> sendCs2Rcon(Cs2InputModel cmd) throws IxigoException;
}
