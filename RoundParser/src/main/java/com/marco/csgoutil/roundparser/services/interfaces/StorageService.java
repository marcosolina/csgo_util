package com.marco.csgoutil.roundparser.services.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.marco.csgoutil.roundparser.model.rest.FileInfo;
import com.marco.utils.MarcoException;

public interface StorageService {
    
    void init(String rootDemFolder);
    
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
    public Map<String, List<FileInfo>> loadAllFileNames() throws MarcoException;
}
