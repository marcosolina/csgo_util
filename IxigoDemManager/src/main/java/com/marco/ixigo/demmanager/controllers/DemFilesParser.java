package com.marco.ixigo.demmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marco.ixigo.demmanager.services.interfaces.DemFileParser;
import com.marco.utils.MarcoException;

@Controller
@RequestMapping(value = "/parse")
public class DemFilesParser {
    
    private static final Logger _LOGGER = LoggerFactory.getLogger(DemFilesParser.class);
    
    @Autowired
    private DemFileParser parser;
    
    @PostMapping
    public ResponseEntity<Void> processNewFiles() {
        _LOGGER.trace("Inside DemFilesProcessor.processNewFiles");
        try {
           parser.processFiles();
        } catch (MarcoException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
