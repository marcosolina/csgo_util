package com.ixigo.models.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ixigo.enums.BotConfigKey;
import com.ixigo.enums.BotConfigValueType;

import io.swagger.annotations.ApiModelProperty;

public class RestBotConfig  implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("config_key")
	@ApiModelProperty(notes = "The config property type", required = true)
	private BotConfigKey configKey;
	@JsonProperty("config_value")
	@ApiModelProperty(notes = "The value of the config property", required = true)
	private String configVal;
	@JsonProperty("config_value_type")
	@ApiModelProperty(notes = "The data type of config value", required = true)
	private BotConfigValueType configValueType;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
