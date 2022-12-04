package com.ixigo.discordbot.repositories.interfaces;

import com.ixigo.discordbot.enums.BotConfigKey;
import com.ixigo.discordbot.models.repo.Bot_configDto;

import reactor.core.publisher.Mono;

public interface RepoBotConfig {
	public Mono<Bot_configDto> fingConfig(BotConfigKey key);

	public Mono<Boolean> insertOtUpdate(Bot_configDto dto);
}
