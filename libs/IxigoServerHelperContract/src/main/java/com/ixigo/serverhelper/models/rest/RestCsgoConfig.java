package com.ixigo.serverhelper.models.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class RestCsgoConfig {
	@JsonProperty("csgo_port")
	@ApiModelProperty(notes = "The port where the CSGO server is running", required = false)
	private int port;
	@JsonProperty("csgo_host")
	@ApiModelProperty(notes = "The host or IP where the CSGO server is running", required = false)
	private String hostName;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
}
