package com.ixigo.demmanager.config.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.services.implementations.DemFileManagerImp;
import com.ixigo.demmanager.services.implementations.DemFileParserImp;
import com.ixigo.demmanager.services.implementations.TelegramNotificationService;
import com.ixigo.demmanager.services.implementations.demprocessor.DemProcessorRasp;
import com.ixigo.demmanager.services.implementations.demprocessor.DemProcessorWindows;
import com.ixigo.demmanager.services.interfaces.DemFileManager;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanager.services.interfaces.DemProcessor;
import com.ixigo.demmanager.services.interfaces.NotificationService;

/**
 * Service layer bean configuration file
 * 
 * @author Marco
 *
 */
@Configuration
public class Services {
	@Autowired
	public DemFileManagerProps props;
	
	@Bean
	public DemFileManager getDemFileManager() {
		return new DemFileManagerImp();
	}
	
	@Bean
	public DemFileParser getDemFileParser() {
		return new DemFileParserImp();
	}
	
	@Bean
    public NotificationService getNotificationService() {
        return new TelegramNotificationService();
    }
	
	@Bean
    public DemProcessor getDemProcessor() {
        switch (props.getParserEnvironment()) {
        case RASP:
            return new DemProcessorRasp();
        default:
            return new DemProcessorWindows();
        }
    }
}