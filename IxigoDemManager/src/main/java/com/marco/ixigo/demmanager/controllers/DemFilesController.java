package com.marco.ixigo.demmanager.controllers;

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

import com.marco.ixigo.demmanager.model.rest.FileInfo;
import com.marco.ixigo.demmanager.model.rest.GetFilesResponse;
import com.marco.ixigo.demmanager.services.interfaces.DemFileManager;
import com.marco.utils.MarcoException;

@Controller
@RequestMapping(value = "/files")
public class DemFilesController {
    private static final Logger _LOGGER = LoggerFactory.getLogger(DemFilesController.class);

    @Autowired
    private DemFileManager fileManagerService;

    @PostMapping
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file) {
        _LOGGER.trace("Inside FilesController.handleFileUpload");
        try {
            fileManagerService.store(file);
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
            file = fileManagerService.load(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (MarcoException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<GetFilesResponse> getListFiles() {
        _LOGGER.trace("Inside FilesController.getListFiles");
        GetFilesResponse resp = new GetFilesResponse();
        try {

            Map<String, List<FileInfo>> games = fileManagerService.loadAllFileNames();
            games.forEach((k, v) -> {
                v.stream().forEach(fi -> {
                    String url = MvcUriComponentsBuilder
                        .fromMethodName(DemFilesController.class, "getFile", fi.getName()).build()
                        .toString();
                    fi.setUrl(url);
                });
            });

            resp.setFiles(games);
            resp.setStatus(true);
        } catch (MarcoException e) {
            resp.addError(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}
