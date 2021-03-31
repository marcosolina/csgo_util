package com.marco.csgoutil.roundparser.services.implementations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.marco.csgoutil.roundparser.model.service.UserMapStats;
import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.utils.MarcoException;

public class CsgoRoundFileParserMarco implements CsgoRoundFileParser {

	private static final Logger _LOGGER = LoggerFactory.getLogger(CsgoRoundFileParserMarco.class);
	
	@Value("${com.marco.csgoutil.roundparser.dotnetexecutable}")
	private String executable;

	@Override
	public List<UserMapStats> extractPlayersScore(File roundFile) throws MarcoException {
		_LOGGER.trace("Inside CsgoRoundFileParserMarco.extractPlayersScore");
		
		List<String> cmd = new ArrayList<>();

		cmd.add("dotnet");
		cmd.add(executable);
		cmd.add(roundFile.getAbsolutePath());

		List<UserMapStats> usersStats = new ArrayList<>();

		try {
			_LOGGER.debug(String.format("Executing command: %s", cmd.toString()));
			
			Process p = Runtime.getRuntime().exec(cmd.toArray(new String[cmd.size()]));
			p.waitFor();
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			String line = null;
			String err = null;
			boolean healtyDem = false;
			
			while ((line = stdInput.readLine()) != null) {
				String [] tmp = line.split(",");
				UserMapStats ums = new UserMapStats();
				ums.setScore(Integer.parseInt(tmp[0]));
				ums.setUserName(tmp[1]);
				ums.setSteamID(tmp[2]);
				usersStats.add(ums);
				healtyDem = true;
			}
			
			StringBuilder sb = new StringBuilder();
			while ((err = stdError.readLine()) != null) {
				sb.append(err);
			}
			
			if(sb.length() > 0 || !healtyDem) {
				throw new MarcoException(sb.toString());
			}
			
		} catch (IOException | InterruptedException e) {
			if (_LOGGER.isTraceEnabled()) {
				e.printStackTrace();
			}
			throw new MarcoException(e.getMessage());
		}
		return usersStats;
	}

}
