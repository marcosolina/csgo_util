package com.ixigo.serverhelper.services.implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.serverhelper.config.properties.EventProperties;
import com.ixigo.serverhelper.services.interfaces.IxigoEventFileReader;

public class IxigoEventFileReaderTail implements IxigoEventFileReader {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoEventFileReaderTail.class);
	@Autowired
	private EventProperties props;
	
	@Override
	public String getCurrentValue() {
		String cmd = String.format("tail %s", props.getEventFilePath().toAbsolutePath().toString());
		_LOGGER.trace(cmd);

		try {
			String err = null;
			String input = null;
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			StringBuilder sbError = new StringBuilder();
			StringBuilder sbInput = new StringBuilder();

			while ((err = stdError.readLine()) != null) {
				sbError.append(err);
			}

			while ((input = stdInput.readLine()) != null) {
				sbInput.append(input);
			}

			if (sbError.length() == 0) {
				return sbInput.toString();
			} else {
				_LOGGER.error(sbError.toString());
			}
		} catch (IOException | InterruptedException e) {
			_LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
