package com.ixigo.discordbot.models.svc.discord;

import com.ixigo.enums.BotConfigKey;
import com.ixigo.enums.BotConfigValueType;

public class SvcBotConfig {
	private BotConfigKey configKey;
	private String configVal;
	private BotConfigValueType configValueType;

	public SvcBotConfig() {

	}
	
	public SvcBotConfig(BotConfigKey configKey, String configVal, BotConfigValueType configValueType) {
		super();
		this.configKey = configKey;
		this.configVal = configVal;
		this.configValueType = configValueType;
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

	public BotConfigValueType getConfigValueType() {
		return configValueType;
	}

	public void setConfigValueType(BotConfigValueType configValueType) {
		this.configValueType = configValueType;
	}

}
