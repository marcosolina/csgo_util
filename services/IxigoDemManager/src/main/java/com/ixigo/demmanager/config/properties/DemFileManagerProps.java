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
	/** How many .dem files are parsed at the same time; when set it overrides processFilesInParallel */
	public Integer parallelParsers;
	public Path parserCs2ExecPath;

	/**
	 * Number of parser processes that may run at the same time: parallelParsers when set, otherwise one per CPU
	 * if processFilesInParallel is true, otherwise 1.
	 */
	public int effectiveParallelParsers() {
		if (parallelParsers != null && parallelParsers > 0) {
			return parallelParsers;
		}
		return Boolean.TRUE.equals(processFilesInParallel) ? Runtime.getRuntime().availableProcessors() : 1;
	}

	public Integer getParallelParsers() {
		return parallelParsers;
	}

	public void setParallelParsers(Integer parallelParsers) {
		this.parallelParsers = parallelParsers;
	}

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

	public Path getParserCs2ExecPath() {
		return parserCs2ExecPath;
	}

	public void setParserCs2ExecPath(Path parserCs2ExecPath) {
		this.parserCs2ExecPath = parserCs2ExecPath;
	}
}
