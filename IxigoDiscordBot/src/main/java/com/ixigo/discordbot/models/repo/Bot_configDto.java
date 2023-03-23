package com.ixigo.discordbot.models.repo;

import com.ixigo.enums.BotConfigKey;
import com.ixigo.enums.BotConfigValueType;
import com.ixigo.library.dto.IxigoDto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class Bot_configDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String config_val = "";
	private BotConfigKey config_key;
	private BotConfigValueType config_value_type;

	public String getConfig_val() {
		return config_val;
	}

	public void setConfig_val(String config_val) {
		this.config_val = config_val;
	}

	public BotConfigKey getConfig_key() {
		return config_key;
	}

	public void setConfig_key(BotConfigKey config_key) {
		this.config_key = config_key;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BotConfigValueType getConfig_value_type() {
		return config_value_type;
	}

	public void setConfig_value_type(BotConfigValueType config_value_type) {
		this.config_value_type = config_value_type;
	}
}
