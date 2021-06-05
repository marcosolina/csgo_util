package com.marco.csgoutil.roundparser.services.interfaces;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.marco.utils.MarcoException;

public interface StorageService {
    /**
     * It saves the files
     * 
     * @param file
     * @throws MarcoException
     */
    void store(MultipartFile file) throws MarcoException;

    /**
     * It returns the file
     * 
     * @param filename
     * @return
     */
    public Resource load(String filename) throws MarcoException;

    /**
     * It returns a list of available Paths
     * 
     * @return
     * @throws MarcoException
     */
    public Stream<Path> loadAll() throws MarcoException;
}
