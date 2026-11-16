package com.ixigo.discordbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.ixigo-server")
public class CsgoServerProps {
	private int port;
	private String hostName;
	private String rconPassword;

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

	public String getRconPassword() {
		return rconPassword;
	}

	public void setRconPassword(String rconPassword) {
		this.rconPassword = rconPassword;
	}

}
