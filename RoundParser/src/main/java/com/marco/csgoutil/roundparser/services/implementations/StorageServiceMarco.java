package com.marco.csgoutil.roundparser.services.implementations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.marco.csgoutil.roundparser.model.rest.FileInfo;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.StorageService;
import com.marco.utils.DateUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DateFormats;

public class StorageServiceMarco implements StorageService {
    private static final Logger _LOGGER = LoggerFactory.getLogger(StorageServiceMarco.class);

    @Autowired
    private RoundFileService roundFildeService;
    private Path root = Paths.get("C:\\tmp");
    
    @Override
    public void init(String rootDemFolder) {
        root = Paths.get(rootDemFolder);
    }

    @Override
    public void store(MultipartFile file) throws MarcoException {
        try {
            String folderName = getFolderFromFileName(file.getOriginalFilename());
            Path fileFolder = this.root.resolve(folderName);
            Files.createDirectories(fileFolder);
            Files.copy(file.getInputStream(), fileFolder.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            _LOGGER.error(e.getMessage());
            throw new MarcoException(e);
        }
    }

    @Override
    public Resource load(String filename) throws MarcoException {
        try {
            String folderName = getFolderFromFileName(filename);
            Path file = root.resolve(folderName).resolve(filename);
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
    public Map<String, List<FileInfo>> loadAllFileNames() throws MarcoException {
        Map<String, List<FileInfo>> map = new TreeMap<>();
        
        roundFildeService.retrieveAllDemFiles().stream().forEach(f -> {
            String folderName = getFolderFromFileName(f.getName());
            map.compute(folderName, (k, v) -> {
                if(v == null) {
                    v = new ArrayList<>();
                }
                FileInfo fi = new FileInfo();
                fi.setName(f.getName());
                fi.setMapName(getMapFromFileName(f.getName()));

                // Get length of file in bytes
                long fileSizeInBytes = f.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;
                
                fi.setSize(new BigDecimal(fileSizeInMB).setScale(2, RoundingMode.DOWN).toString() + " MB");
                v.add(fi);
                
                return v;
            });
        });
        
        return map;
    }
    
    private String getFolderFromFileName(String fileName) {
        String [] arr = fileName.split("-");
        return DateUtils.fromLocalDateToString(DateUtils.fromStringToLocalDate(arr[1], DateFormats.FOLDER_NAME), DateFormats.DB_DATE);
    }

    private String getMapFromFileName(String fileName) {
        String [] arr = fileName.split("-");
        return arr[4];
    }
}
