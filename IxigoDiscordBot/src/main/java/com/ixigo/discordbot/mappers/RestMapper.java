package com.ixigo.discordbot.mappers;

import org.mapstruct.Mapper;

import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;
import com.ixigo.discordbot.models.svc.discord.SvcDiscordUser;
import com.ixigo.discordbot.models.svc.discord.SvcPlayer;
import com.ixigo.models.rest.RestBotConfig;
import com.ixigo.models.rest.RestDiscordUser;
import com.ixigo.models.rest.RestPlayer;

@Mapper(componentModel = "spring")
public interface RestMapper {
	RestBotConfig fromSvcToRest(SvcBotConfig svc);

	SvcBotConfig fromRestToSvc(RestBotConfig rest);

	RestPlayer fromSvcToRest(SvcPlayer svc);

	SvcPlayer fromRestToSvc(RestPlayer svc);
	
	RestDiscordUser fromSvcToRest(SvcDiscordUser svc);
}
