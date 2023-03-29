package com.ixigo.rconapi.models.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class RestRconRequest implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonProperty("rcon_host")
	@ApiModelProperty(notes = "The host that is running the RCON server", required = true)
	private String rconHost;
	
	@JsonProperty("rcon_port")
	@ApiModelProperty(notes = "The RCON port that the server is listening to", required = true)
	private int rconPort;
	
	@JsonProperty("rcon_passw")
	@ApiModelProperty(notes = "The RCON password", required = true)
	private String rconPass;
	
	@JsonProperty("rcon_command")
	@ApiModelProperty(notes = "The RCON command to execute", required = true)
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
