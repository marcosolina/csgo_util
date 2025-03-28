package com.ixigo.discordbot.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ixigo.demmanagercontract.models.rest.demdata.data.RestUsers;
import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;
import com.ixigo.discordbot.models.svc.discord.SvcDiscordUser;
import com.ixigo.discordbot.models.svc.discord.SvcPlayer;
import com.ixigo.discordbot.models.svc.discord.SvcSteamTeams;
import com.ixigo.models.rest.RestBotConfig;
import com.ixigo.models.rest.RestDiscordUser;
import com.ixigo.models.rest.RestPlayer;
import com.ixigo.models.rest.RestSteamTeams;
import com.ixigo.models.rest.RestUser;

@Mapper(componentModel = "spring")
public interface RestMapper {
	RestBotConfig fromSvcToRest(SvcBotConfig svc);

	SvcBotConfig fromRestToSvc(RestBotConfig rest);

	RestPlayer fromSvcToRest(SvcPlayer svc);

	@Mapping(source = "steamDetails.userName", target = "steamDetails.user_name")
	@Mapping(source = "steamDetails.steamId", target = "steamDetails.steam_id")
	SvcPlayer fromRestToSvc(RestPlayer svc);
	
	@Mapping(source = "user_name", target = "userName")
	@Mapping(source = "steam_id", target = "steamId")
	RestUser fromDemManagerUsersToDiscordBotUser(RestUsers demUsers);
	
	RestDiscordUser fromSvcToRest(SvcDiscordUser svc);
	
	SvcSteamTeams fromRestToSvc(RestSteamTeams rest);
}
