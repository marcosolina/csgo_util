package com.ixigo.discordbot.repositories.interfaces;

import com.ixigo.discordbot.models.repo.Bot_configDto;
import com.ixigo.enums.BotConfigKey;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepoBotConfig {
	public Mono<Bot_configDto> fingConfig(BotConfigKey key);
	
	public Flux<Bot_configDto> findAllConfig();

	public Mono<Boolean> insertOtUpdate(Bot_configDto dto);
}
