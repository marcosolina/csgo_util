package com.ixigo.discordbot.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ixigo.discordbot.models.repo.Bot_configDto;
import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;

@Mapper(componentModel = "spring")
public interface RepoMapper {
	@Mapping(source = "configKey", target = "config_key")
	@Mapping(source = "configVal", target = "config_val")
	Bot_configDto raceFromSvcToDto(SvcBotConfig svc);

	@Mapping(source = "config_key", target = "configKey")
	@Mapping(source = "config_val", target = "configVal")
	SvcBotConfig raceFromDtoSvcToSvc(Bot_configDto dto);
}
