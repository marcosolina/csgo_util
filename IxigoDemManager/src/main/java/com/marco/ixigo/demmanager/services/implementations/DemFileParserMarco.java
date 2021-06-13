package com.marco.ixigo.demmanager.services.implementations;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.marco.ixigo.demmanager.enums.ParserExecutionType;
import com.marco.ixigo.demmanager.model.entities.EntityProcessQueue;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.marco.ixigo.demmanager.services.interfaces.DemFileParser;
import com.marco.utils.MarcoException;

public class DemFileParserMarco implements DemFileParser {
    private static final Logger _LOGGER = LoggerFactory.getLogger(DemFileParserMarco.class);

    @Autowired
    private RepoProcessQueue repoQueue;
    @Value("${com.marco.ixigo.demmanager.demparser.executionType}")
    private ParserExecutionType execType;

    @Override
    public boolean processFiles() throws MarcoException {
        _LOGGER.debug("Processing all the new files files");
        List<EntityProcessQueue> filesEntities = repoQueue.getNotProcessedDemFiles();
        List<File> filesToProcess = filesEntities.stream().map(e -> {
            return new File(e.getFileName());
        }).collect(Collectors.toList());
        
        switch (execType) {
        case SYNC:
            processFiles(filesToProcess);
            break;

        default:
            new Thread(() -> processFiles(filesToProcess)).start();
            break;
        }

        return false;
    }
    
    private void processFiles(List<File> files) {
        
    }

}
