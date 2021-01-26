package com.marco.csgorestapi.model;

public class RconHttpRequest {
	private String rconHost;
	private int rconPort;
	private String rconPass;
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
