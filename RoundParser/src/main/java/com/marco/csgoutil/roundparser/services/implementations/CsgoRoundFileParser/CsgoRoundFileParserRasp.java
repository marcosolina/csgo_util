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
 * This is the implementation that I wrote for my Raspberry env
 * 
 * @author Marco
 *
 */
public class CsgoRoundFileParserRasp implements CsgoRoundFileParser {

	private static final Logger _LOGGER = LoggerFactory.getLogger(CsgoRoundFileParserRasp.class);

	@Value("${com.marco.csgoutil.roundparser.dotnetexecutable}")
	private String executable;

	@Override
	public List<UserMapStats> extractPlayersScore(File roundFile) throws MarcoException {
		_LOGGER.trace("Inside CsgoRoundFileParserRasp.extractPlayersScore");

		List<String> cmd = new ArrayList<>();

		cmd.add(executable);
		cmd.add(roundFile.getAbsolutePath());

		CmdExecuter ce = new CmdExecuter();
		return ce.extractPlayersScore(cmd);
	}

}
