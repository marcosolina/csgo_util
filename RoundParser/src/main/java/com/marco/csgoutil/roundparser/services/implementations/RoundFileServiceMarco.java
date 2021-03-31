package com.marco.csgoutil.roundparser.services.implementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.utils.MarcoException;

/**
 * My implementation of this inteface
 * 
 * @author Marco
 *
 */
public class RoundFileServiceMarco implements RoundFileService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RoundFileServiceMarco.class);

	@Value("${com.marco.csgoutil.roundparser.demfolder}")
	private String demFolder;

	@Override
	public List<File> retrieveAllDemFiles() throws MarcoException {
		List<File> files = new ArrayList<>();
		try (Stream<Path> walk = Files.walk(Paths.get(demFolder))) {
			walk.filter(p -> p.toFile().getName().endsWith(".dem")).map(Path::toFile).forEach(files::add);
		} catch (IOException e) {
			if (_LOGGER.isTraceEnabled()) {
				e.printStackTrace();
			}
			throw new MarcoException(e);
		}
		return files;
	}

}
