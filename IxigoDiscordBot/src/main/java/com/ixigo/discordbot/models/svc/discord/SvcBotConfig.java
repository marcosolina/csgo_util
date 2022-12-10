package com.ixigo.discordbot.models.svc.discord;

import com.ixigo.enums.BotConfigKey;

public class SvcBotConfig {
	private BotConfigKey configKey;
	private String configVal;

	public SvcBotConfig() {

	}

	public SvcBotConfig(BotConfigKey configKey, String configVal) {
		this.configKey = configKey;
		this.configVal = configVal;
	}

	public BotConfigKey getConfigKey() {
		return configKey;
	}

	public void setConfigKey(BotConfigKey configKey) {
		this.configKey = configKey;
	}

	public String getConfigVal() {
		return configVal;
	}

	public void setConfigVal(String configVal) {
		this.configVal = configVal;
	}
}
