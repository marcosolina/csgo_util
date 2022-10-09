package com.ixigo.demmanager.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.demmanager.services.implementations.DemFileManagerImp;
import com.ixigo.demmanager.services.implementations.DemFileParserImp;
import com.ixigo.demmanager.services.interfaces.DemFileManager;
import com.ixigo.demmanager.services.interfaces.DemFileParser;

/**
 * Service layer bean configuration file
 * 
 * @author Marco
 *
 */
@Configuration
public class Services {

	@Bean
	public DemFileManager getDemFileManager() {
		return new DemFileManagerImp();
	}
	
	@Bean
	public DemFileParser getDemFileParser() {
		return new DemFileParserImp();
	}
}
