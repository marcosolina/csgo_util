package com.ixigo.serverhelper.models.svc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SvcCsgoConfig {
	@JsonProperty("csgo_port")
	private int port;
	@JsonProperty("csgo_host")
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
