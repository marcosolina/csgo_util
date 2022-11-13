package com.ixigo.rconapi.models.rest;

import io.swagger.annotations.ApiModelProperty;

public class RestRconRequest {
	@ApiModelProperty(notes = "The host that is running the RCON server", required = true)
	private String rconHost;
	@ApiModelProperty(notes = "The RCON port that the server is listening to", required = true)
	private int rconPort;
	@ApiModelProperty(notes = "The RCON password", required = true)
	private String rconPass;
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
