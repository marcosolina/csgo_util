package com.marco.csgoutil.roundparser.services.implementations.CsgoRoundFileParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.marco.csgoutil.roundparser.model.service.UserMapStats;
import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.utils.MarcoException;

/**
 * Implementation for windows environments
 * 
 * @author Marco
 *
 */
public class CsgoRoundFileParserWindows implements CsgoRoundFileParser {
	private static final Logger _LOGGER = LoggerFactory.getLogger(CsgoRoundFileParserWindows.class);

	@Value("${com.marco.csgoutil.roundparser.dotnetexecutable}")
	private String executable;

	@Override
	public List<UserMapStats> extractPlayersScore(File roundFile) throws MarcoException {
		_LOGGER.trace("Inside CsgoRoundFileParserWindows.extractPlayersScore");

		List<String> cmd = new ArrayList<>();
		cmd.add("dotnet");
		cmd.add(executable);
		cmd.add(roundFile.getAbsolutePath());

		CmdExecuter ce = new CmdExecuter();
		return ce.extractPlayersScore(cmd);
	}
}
