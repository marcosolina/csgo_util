package com.marco.csgoutil.roundparser.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.marco.csgoutil.rconapi")
public class RconProperties {
	private String protocol;
	private String ip;
	private String endpoint;
	private String csgoserverip;
	private String csgorconpassw;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getCsgoserverip() {
		return csgoserverip;
	}

	public void setCsgoserverip(String csgoserverip) {
		this.csgoserverip = csgoserverip;
	}

	public String getCsgorconpassw() {
		return csgorconpassw;
	}

	public void setCsgorconpassw(String csgorconpassw) {
		this.csgorconpassw = csgorconpassw;
	}

}
