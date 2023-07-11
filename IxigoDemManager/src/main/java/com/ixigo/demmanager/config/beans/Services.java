package com.ixigo.demmanager.config.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.enums.ParserEnvironment;
import com.ixigo.demmanager.enums.ParserTool;
import com.ixigo.demmanager.services.implementations.ChartsDataImp;
import com.ixigo.demmanager.services.implementations.DemFileManagerImp;
import com.ixigo.demmanager.services.implementations.DemFileParserImp;
import com.ixigo.demmanager.services.implementations.TelegramNotificationService;
import com.ixigo.demmanager.services.implementations.demprocessor.CmdExecuterImpl;
import com.ixigo.demmanager.services.implementations.demprocessor.csharp.DemProcessorRasp;
import com.ixigo.demmanager.services.implementations.demprocessor.csharp.DemProcessorWindows;
import com.ixigo.demmanager.services.implementations.demprocessor.nodejs.DemProcessorNodeJsLinux;
import com.ixigo.demmanager.services.implementations.demprocessor.nodejs.DemProcessorNodeJsWindows;
import com.ixigo.demmanager.services.interfaces.ChartsData;
import com.ixigo.demmanager.services.interfaces.CmdExecuter;
import com.ixigo.demmanager.services.interfaces.DemFileManager;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanager.services.interfaces.DemProcessor;
import com.ixigo.demmanager.services.interfaces.NotificationService;

/**
 * Configuration of the beans used for the service layer
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
	public CmdExecuter getCmdExecuter() {
		return new CmdExecuterImpl();
	}
	
	@Bean
	public ChartsData getChartsData() {
		return new ChartsDataImp();
	}

	@Bean
	public DemProcessor getDemProcessor() {
		boolean useCSharpTool = props.getParserTool() == ParserTool.C_SHARP;
		boolean runningOnWindows = props.getParserEnvironment() == ParserEnvironment.WINDOWS;
		
		if(useCSharpTool) {
			return runningOnWindows ? new DemProcessorRasp() : new DemProcessorWindows();
		}
		
		return runningOnWindows ? new DemProcessorNodeJsWindows() : new DemProcessorNodeJsLinux();
	}
}
