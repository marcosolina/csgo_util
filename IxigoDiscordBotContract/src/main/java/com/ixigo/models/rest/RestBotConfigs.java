package com.ixigo.models.rest;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class RestBotConfigs implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("configs")
	@ApiModelProperty(notes = "The configurations available", required = true)
	private List<RestBotConfig> configs;

	public List<RestBotConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(List<RestBotConfig> configs) {
		this.configs = configs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
