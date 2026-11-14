package com.ixigo.discordbot.commands.config;

import com.ixigo.enums.BotConfigKey;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;
import com.ixigo.models.rest.RestBotConfig;

public class GetBotConfigCmd implements WebCommandRequest<RestBotConfig> {
	private BotConfigKey key;

	public GetBotConfigCmd(BotConfigKey key) {
		super();
		this.key = key;
	}

	public BotConfigKey getKey() {
		return key;
	}

	public void setKey(BotConfigKey key) {
		this.key = key;
	}

}
