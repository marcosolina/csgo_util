package com.ixigo.serverhelper.services.implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.serverhelper.constants.ErrorCodes;
import com.ixigo.serverhelper.services.interfaces.Cs2SendInputs;

import reactor.core.publisher.Mono;

public class Cs2SendInputsScreen implements Cs2SendInputs {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Cs2SendInputsScreen.class);

	@Override
	public Mono<Boolean> sendInput(String input) {

		// screen -S minecraft -p 0 -X stuff "stop^M"
		List<String> cmd = new ArrayList<>();
		cmd.add("screen");
		cmd.add("-S");
		cmd.add("cs2");
		cmd.add("-p");
		cmd.add("0");
		cmd.add("-X");
		cmd.add("stuff");
		cmd.add(String.format("%s^M", input));

		return Mono.fromSupplier(() -> {
			_LOGGER.debug(String.format("Executing command: %s", cmd.toString()));
			try {
				ProcessBuilder builder = new ProcessBuilder(cmd);
				Process p = builder.start();

				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String err = null;

				StringBuilder sbError = new StringBuilder();
				while ((err = stdError.readLine()) != null) {
					sbError.append(err);
				}

				if (sbError.length() > 0) {
					throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, sbError.toString(),
							ErrorCodes.GENERIC_ERROR);
				}

				int code = p.waitFor();
				_LOGGER.debug("CMD exited with status " + code);

				return true;
			} catch (IOException | InterruptedException | NumberFormatException e) {
				e.printStackTrace();
				throw new IxigoException(HttpStatus.BAD_GATEWAY, e.getMessage(), ErrorCodes.GENERIC_ERROR);
			}
		});
	}

}
