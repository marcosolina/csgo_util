package com.marco.csgoutil.roundparser.model.service;

public class RconCmd {
	private String rconCmd;
	private String rconHost;
	private String rconPass;
	private Integer rconPort;

	public String getRconCmd() {
		return rconCmd;
	}

	public void setRconCmd(String rconCmd) {
		this.rconCmd = rconCmd;
	}

	public String getRconHost() {
		return rconHost;
	}

	public void setRconHost(String rconHost) {
		this.rconHost = rconHost;
	}

	public String getRconPass() {
		return rconPass;
	}

	public void setRconPass(String rconPass) {
		this.rconPass = rconPass;
	}

	public Integer getRconPort() {
		return rconPort;
	}

	public void setRconPort(Integer rconPort) {
		this.rconPort = rconPort;
	}
}
