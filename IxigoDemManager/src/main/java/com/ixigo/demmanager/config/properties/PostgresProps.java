package com.ixigo.demmanager.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties for the Database
 * 
 * @author marco
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ixigo.postgres")
public class PostgresProps {
	private String host;
	private String user;
	private String password;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "PostgresProps [host=" + host + ", user=" + user + ", password=" + password + "]";
	}
}
