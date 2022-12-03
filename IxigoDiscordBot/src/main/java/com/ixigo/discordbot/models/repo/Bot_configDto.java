package com.ixigo.discordbot.models.repo;

import com.ixigo.library.dto.IxigoDto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class Bot_configDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String config_val = "";
	private String config_key = "";

	public String getConfig_val() {
		return this.config_val;
	}

	public void setConfig_val(String config_val) {
		this.config_val = config_val;
	}

	public String getConfig_key() {
		return this.config_key;
	}

	public void setConfig_key(String config_key) {
		this.config_key = config_key;
	}

}
