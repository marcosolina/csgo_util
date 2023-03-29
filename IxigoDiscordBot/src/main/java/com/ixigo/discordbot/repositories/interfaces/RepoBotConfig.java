package com.ixigo.discordbot.repositories.interfaces;

import com.ixigo.discordbot.models.repo.Bot_configDto;
import com.ixigo.enums.BotConfigKey;

import reactor.core.publisher.Mono;

public interface RepoBotConfig {
	public Mono<Bot_configDto> fingConfig(BotConfigKey key);

	public Mono<Boolean> insertOtUpdate(Bot_configDto dto);
}
