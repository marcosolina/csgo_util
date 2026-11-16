package com.ixigo.demmanager.services.implementations.demprocessor.nodejs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcNodeJsParseOutput;
import com.ixigo.demmanager.services.interfaces.CmdExecuter;
import com.ixigo.demmanager.services.interfaces.DemFileFormatChecker;
import com.ixigo.demmanager.services.interfaces.DemProcessor;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Mono;

/**
 * Implementation to use when processing DEM files on a Windows machine
 * 
 * @author Marco
 *
 */
public class DemProcessorNodeJsWindows implements DemProcessor {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemProcessorNodeJsWindows.class);

	@Autowired
	private DemFileFormatChecker formatChecker;
	@Autowired
	private DemFileManagerProps props;
	@Autowired
	private CmdExecuter exec;

	@Override
	public Mono<SvcNodeJsParseOutput> processDemFile(File demFile) throws IxigoException {
		_LOGGER.trace("Inside DemProcessorWindows.DemProcessorWindows");

		return formatChecker.isCs2DemFile(demFile).flatMap(isCs2DemFile -> {
			List<String> cmd = new ArrayList<>();
			cmd.add("npx.cmd");
			cmd.add("ts-node");
			cmd.add(isCs2DemFile ? props.parserCs2ExecPath.toString() : props.parserExecPath.toString());
			cmd.add(demFile.getAbsolutePath());

			return exec.runCommand(cmd).map(s -> {
				try {
					ObjectMapper objectMapper = JsonMapper.builder()
							.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true) // Configure to ignore case during deserialization
							.build();
					objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
					objectMapper.setVisibility(
							VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
					
					SvcNodeJsParseOutput output = objectMapper.readValue(s, SvcNodeJsParseOutput.class);
					output.setCs2DemFile(isCs2DemFile);// TODO make it better
					return output;
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC);
				}
			});
		});

	}

}
