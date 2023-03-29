package com.ixigo.rconapi.models.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class SvcRconRequest {
	@JsonProperty("rcon_host")
	private String rconHost;
	
	@JsonProperty("rcon_port")
	private int rconPort;
	
	@JsonProperty("rcon_passw")
	private String rconPass;
	
	@JsonProperty("rcon_command")
	private String rconCmd;

	public String getRconHost() {
		return rconHost;
	}

	public void setRconHost(String rconHost) {
		this.rconHost = rconHost;
	}

	public int getRconPort() {
		return rconPort;
	}

	public void setRconPort(int rconPort) {
		this.rconPort = rconPort;
	}

	public String getRconPass() {
		return rconPass;
	}

	public void setRconPass(String rconPass) {
		this.rconPass = rconPass;
	}

	public String getRconCmd() {
		return rconCmd;
	}

	public void setRconCmd(String rconCmd) {
		this.rconCmd = rconCmd;
	}
}
