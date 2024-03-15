package com.ixigo.serverhelper.config.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.serverhelper.config.properties.EventProperties;
import com.ixigo.serverhelper.enums.EventFileReaderImplementations;
import com.ixigo.serverhelper.services.implementations.Cs2SendInputsScreen;
import com.ixigo.serverhelper.services.implementations.CsgoServerConfigManagerImp;
import com.ixigo.serverhelper.services.implementations.DemFilesServiceImp;
import com.ixigo.serverhelper.services.implementations.DyndnsUpdater;
import com.ixigo.serverhelper.services.implementations.IxiGoEventMonitorImp;
import com.ixigo.serverhelper.services.implementations.IxigoEventFileReaderPowerShell;
import com.ixigo.serverhelper.services.implementations.IxigoEventFileReaderTail;
import com.ixigo.serverhelper.services.interfaces.Cs2SendInputs;
import com.ixigo.serverhelper.services.interfaces.CsgoServerConfigManager;
import com.ixigo.serverhelper.services.interfaces.DemFilesService;
import com.ixigo.serverhelper.services.interfaces.DnsUpdater;
import com.ixigo.serverhelper.services.interfaces.IxiGoEventMonitor;
import com.ixigo.serverhelper.services.interfaces.IxigoEventFileReader;

@Configuration
public class ServicesBeans {
	@Autowired
	private EventProperties props;

	@Bean
	public IxiGoEventMonitor getIxiGoEventMonitor() {
		return new IxiGoEventMonitorImp();
	}

	@Bean
	public DemFilesService getDemFilesService() {
		return new DemFilesServiceImp();
	}

	@Bean
	public IxigoEventFileReader getIxigoEventFileReader() {
		if (props.getEventFileReaderImpl() == EventFileReaderImplementations.POWERSHELL) {
			return new IxigoEventFileReaderPowerShell();
		}
		return new IxigoEventFileReaderTail();
	}

	@Bean
	public CsgoServerConfigManager getCsgoServerConfigManager() {
		return new CsgoServerConfigManagerImp();
	}

	@Bean
	public DnsUpdater getDnsUpdater() {
		return new DyndnsUpdater();
	}

	@Bean
	public Cs2SendInputs getCs2SendInputs() {
		return new Cs2SendInputsScreen();
	}
}
