package com.ixigo.demmanager.services.implementations.demprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.models.svc.demdata.SvcUserGotvScore;
import com.ixigo.demmanager.services.interfaces.CmdExecuter;
import com.ixigo.demmanager.services.interfaces.DemProcessor;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;

/**
 * Implementation to use when processing DEM files on a Windows machine
 * 
 * @author Marco
 *
 */
public class DemProcessorNodeJsWindows implements DemProcessor {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemProcessorNodeJsWindows.class);

	@Autowired
	private DemFileManagerProps props;
	@Autowired
	private CmdExecuter exec;

	@Override
	public Flux<SvcUserGotvScore> processDemFile(File demFile) throws IxigoException {
		_LOGGER.trace("Inside DemProcessorWindows.DemProcessorWindows");

		List<String> cmd = new ArrayList<>();
		cmd.add("npx.cmd");
		cmd.add("ts-node");
		cmd.add(props.parserExecPath.toString());
		cmd.add(demFile.getAbsolutePath());

		exec.runCommand(cmd).subscribe(s -> {
			_LOGGER.debug(s);
		});
		return null;
	}

}
