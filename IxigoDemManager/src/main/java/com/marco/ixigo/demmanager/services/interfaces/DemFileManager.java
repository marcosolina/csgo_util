package com.marco.ixigo.demmanager.services.interfaces;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.marco.ixigo.demmanager.model.rest.FileInfo;
import com.marco.utils.MarcoException;

public interface DemFileManager {
    /**
     * It saves the file and return the path of the saved file
     * 
     * @param file
     * @return
     * @throws MarcoException
     */
    public Path store(MultipartFile file) throws MarcoException;

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
    public Map<String, List<FileInfo>> loadAllFileNames() throws MarcoException;
}
