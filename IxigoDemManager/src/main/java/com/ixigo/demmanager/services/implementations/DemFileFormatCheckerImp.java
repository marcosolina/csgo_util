package com.ixigo.demmanager.services.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.services.interfaces.DemFileFormatChecker;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;

import reactor.core.publisher.Mono;

public class DemFileFormatCheckerImp implements DemFileFormatChecker {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemFileFormatCheckerImp.class);
	
	@Autowired
	private IxigoMessageResource msgSource;

	@Override
	public Mono<Boolean> isCs2DemFile(File file) {
		var mono = Mono.fromSupplier(() -> {

			try (FileInputStream fis = new FileInputStream(file)) {
				byte[] bytes = new byte[8];
				if (fis.read(bytes) != -1) {
					String header = new String(bytes, StandardCharsets.UTF_8);

					if (header.equals("PBDEMS2\u0000")) {
						return true;
					} else if (header.equals("HL2DEMO\u0000")) {
						return false;
					} else {
						throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(ErrorCodes.ERROR_UNKOWN_DEM_FORMAT), ErrorCodes.ERROR_UNKOWN_DEM_FORMAT);
					}
				}

			} catch (IOException e) {
				String msg = "Error reading the file: " + e.getMessage();
				_LOGGER.error(msg);
				e.printStackTrace();
				throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, msg, ErrorCodes.GENERIC);
			}
			return false;
		});
		return mono;
	}

}
