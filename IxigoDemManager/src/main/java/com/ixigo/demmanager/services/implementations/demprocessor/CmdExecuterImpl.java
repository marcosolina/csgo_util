package com.ixigo.demmanager.services.implementations.demprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.services.interfaces.CmdExecuter;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class CmdExecuterImpl implements CmdExecuter {
	private static final Logger _LOGGER = LoggerFactory.getLogger(CmdExecuterImpl.class);

	@Override
	public Mono<String> runCommand(List<String> cmd) throws IxigoException {
		var mono = Mono.fromSupplier(() -> {
			try {
				_LOGGER.debug(String.format("Executing command: %s", cmd.toString()));

				ProcessBuilder builder = new ProcessBuilder(cmd);
				Process p = builder.start();

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

				StringBuilder sbOutput = new StringBuilder();
				String line = null;
				String err = null;
				while ((line = stdInput.readLine()) != null) {
					sbOutput.append(line);
				}

				StringBuilder sbError = new StringBuilder();
				while ((err = stdError.readLine()) != null) {
					sbError.append(err);
				}

				if (sbError.length() > 0) {
					throw new IxigoException(HttpStatus.BAD_GATEWAY, sbError.toString(), ErrorCodes.GENERIC);
				}

				int code = p.waitFor();
				_LOGGER.debug("CMD exited with status " + code);

				return sbOutput.toString();

			} catch (IOException | InterruptedException | NumberFormatException e) {
				e.printStackTrace();
				throw new IxigoException(HttpStatus.BAD_GATEWAY, e.getMessage(), ErrorCodes.GENERIC);
			}
		}).subscribeOn(Schedulers.boundedElastic());
		return mono;
	}
}
