package com.marco.csgoutil.roundparser.services.implementations;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.marco.csgoutil.roundparser.services.interfaces.StorageService;
import com.marco.utils.MarcoException;

public class StorageServiceMarco implements StorageService {
    private static final Logger _LOGGER = LoggerFactory.getLogger(StorageServiceMarco.class);

    private final Path root = Paths.get("C:\\tmp");

    @Override
    public void store(MultipartFile file) throws MarcoException {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            _LOGGER.error(e.getMessage());
            throw new MarcoException(e);
        }
    }

    @Override
    public Resource load(String filename) throws MarcoException {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new MarcoException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new MarcoException("Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() throws MarcoException {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new MarcoException("Could not load the files!");
        }
    }
}
