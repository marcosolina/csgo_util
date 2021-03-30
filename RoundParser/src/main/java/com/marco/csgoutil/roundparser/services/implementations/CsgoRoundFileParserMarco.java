package com.marco.csgoutil.roundparser.services.implementations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.utils.MarcoException;

public class CsgoRoundFileParserMarco implements CsgoRoundFileParser {

	private static final Logger _LOGGER = LoggerFactory.getLogger(CsgoRoundFileParserMarco.class);
	
	@Value("${com.marco.csgoutil.roundparser.dotnetexecutable}")
	private String executable;

	@Override
	public Map<String, Integer> extractPlayersScore(File roundFile) throws MarcoException {
		_LOGGER.trace("Inside CsgoRoundFileParserMarco.extractPlayersScore");
		
		
		List<String> cmd = new ArrayList<>();

		cmd.add("dotnet");
		cmd.add(executable);
		cmd.add(roundFile.getAbsolutePath());

		Map<String, Integer> map = new HashMap<>();

		try {
			_LOGGER.debug(String.format("Executing command: %s", cmd.toString()));
			
			Process p = Runtime.getRuntime().exec(cmd.toArray(new String[cmd.size()]));
			p.waitFor();
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			String line = null;
			String err = null;
			
			while ((line = stdInput.readLine()) != null) {
				String [] tmp = line.split(",");
				map.put(tmp[1], Integer.parseInt(tmp[0]));
			}
			
			StringBuilder sb = new StringBuilder();
			while ((err = stdError.readLine()) != null) {
				sb.append(err);
			}
			
			if(sb.length() > 0) {
				throw new MarcoException(sb.toString());
			}
			
		} catch (IOException | InterruptedException e) {
			if (_LOGGER.isTraceEnabled()) {
				e.printStackTrace();
			}
			throw new MarcoException(e.getMessage());
		}
		return map;
	}

}
