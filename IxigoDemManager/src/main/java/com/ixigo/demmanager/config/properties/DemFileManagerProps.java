package com.ixigo.demmanager.config.properties;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ixigo.demmanager.enums.ParserEnvironment;
import com.ixigo.demmanager.enums.ParserExecutionType;

/**
 * Properties files of the dem manager micro service
 * 
 * @author marco
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ixigo.dem-manager.dem-file-props")
public class DemFileManagerProps {
	public Path rootFolder;
	public ParserEnvironment parserEnvironment;
	public ParserExecutionType parserExecutionType;
	public Path parserExecPath;
	public Boolean processFilesInParallel;

	public Path getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(Path rootFolder) {
		this.rootFolder = rootFolder;
	}

	public ParserEnvironment getParserEnvironment() {
		return parserEnvironment;
	}

	public void setParserEnvironment(ParserEnvironment parserEnvironment) {
		this.parserEnvironment = parserEnvironment;
	}

	public ParserExecutionType getParserExecutionType() {
		return parserExecutionType;
	}

	public void setParserExecutionType(ParserExecutionType parserExecutionType) {
		this.parserExecutionType = parserExecutionType;
	}

	public Path getParserExecPath() {
		return parserExecPath;
	}

	public void setParserExecPath(Path parserExecPath) {
		this.parserExecPath = parserExecPath;
	}

	public Boolean getProcessFilesInParallel() {
		return processFilesInParallel;
	}

	public void setProcessFilesInParallel(Boolean processFilesInParallel) {
		this.processFilesInParallel = processFilesInParallel;
	}

}
