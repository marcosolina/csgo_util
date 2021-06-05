package com.marco.csgoutil.roundparser.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.marco.csgoutil.roundparser.model.rest.FileInfo;
import com.marco.csgoutil.roundparser.model.rest.GetFiles;
import com.marco.csgoutil.roundparser.services.interfaces.StorageService;
import com.marco.utils.MarcoException;

@Controller
@RequestMapping(value = "/files")
public class FilesController {
    private static final Logger _LOGGER = LoggerFactory.getLogger(FilesController.class);

    @Autowired
    private StorageService ss;

    @PostMapping
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file) {
        _LOGGER.trace("Inside FilesController.handleFileUpload");
        try {
            ss.store(file);
        } catch (MarcoException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        _LOGGER.trace("Inside FilesController.getFile");
        Resource file;
        try {
            file = ss.load(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (MarcoException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<GetFiles> getListFiles() {
        _LOGGER.trace("Inside FilesController.getListFiles");
        GetFiles resp = new GetFiles();
        try {

            Map<String, List<FileInfo>> games = ss.loadAllFileNames();
            games.forEach((k, v) -> {
                v.stream().forEach(fi -> {
                    String url = MvcUriComponentsBuilder
                        .fromMethodName(FilesController.class, "getFile", fi.getName()).build()
                        .toString();
                    fi.setUrl(url);
                });
            });

            resp.setFiles(games);
            resp.setStatus(true);
        } catch (MarcoException e) {
            resp.addError(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}
