package com.ixigo.discordbot.mappers;

import org.mapstruct.Mapper;

import com.ixigo.discordbot.models.repo.Bot_configDto;
import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;

@Mapper(componentModel = "spring")
public interface RepoMapper {
	Bot_configDto raceFromSvcToDto(SvcBotConfig svc);
	SvcBotConfig raceFromDtoSvcToSvc(Bot_configDto dto);
}
