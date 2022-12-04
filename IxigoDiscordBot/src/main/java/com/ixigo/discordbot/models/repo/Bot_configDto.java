package com.ixigo.discordbot.models.repo;

import com.ixigo.discordbot.enums.BotConfigKey;
import com.ixigo.library.dto.IxigoDto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class Bot_configDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String config_val = "";
	private BotConfigKey config_key;

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

}
