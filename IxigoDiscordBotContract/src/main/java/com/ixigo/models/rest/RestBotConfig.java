package com.ixigo.models.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ixigo.enums.BotConfigKey;

import io.swagger.annotations.ApiModelProperty;

public class RestBotConfig {
	@JsonProperty("config_key")
	@ApiModelProperty(notes = "The config property type", required = true)
	private BotConfigKey configKey;
	@JsonProperty("config_value")
	@ApiModelProperty(notes = "The value of the config property", required = true)
	private String configVal;

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
