package com.ixigo.serverhelper.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.serverhelper.services.implementations.IxiGoEventMonitorImp;
import com.ixigo.serverhelper.services.interfaces.IxiGoEventMonitor;

@Configuration
public class ServicesBeans {
	@Bean
	public IxiGoEventMonitor getIxiGoEventMonitor() {
		return new IxiGoEventMonitorImp();
	}
}
