package com.ixigo.demmanager.services.implementations.demprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.models.svc.demdata.SvcUserGotvScore;
import com.ixigo.demmanager.services.implementations.DemFileParserImp;
import com.ixigo.demmanager.services.interfaces.DemProcessor;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;

/**
 * Implementation to be used to process the DEM file
 * on a Raspberry PI
 * @author Marco
 *
 */
public class DemProcessorRasp implements DemProcessor {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemFileParserImp.class);
	
	@Autowired
	private DemFileManagerProps props;
	
	@Override
	public Flux<SvcUserGotvScore> processDemFile(File demFile) throws IxigoException {
		_LOGGER.trace("Inside DemProcessorRasp.DemProcessorWindows");

        List<String> cmd = new ArrayList<>();
        cmd.add(props.getParserExecPath().toString());
        cmd.add(demFile.getAbsolutePath());

        CmdExecuter ce = new CmdExecuter();
        return ce.extractPlayersScore(cmd);
	}

}
