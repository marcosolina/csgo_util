package com.marco.ixigo.demmanager.services.implementations;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.marco.ixigo.demmanager.enums.DemProcessStatus;
import com.marco.ixigo.demmanager.model.entities.EntityProcessQueue;
import com.marco.ixigo.demmanager.model.rest.FileInfo;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.marco.ixigo.demmanager.services.interfaces.DemFileManager;
import com.marco.utils.DateUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DateFormats;

public class DemFileManagerMarco implements DemFileManager {
    private static final Logger _LOGGER = LoggerFactory.getLogger(DemFileManagerMarco.class);

    @Value("${com.marco.ixigo.demmanager.demFileManager.rootFolder}")
    private Path root;
    @Autowired
    private RepoProcessQueue repoQueue;

    @Override
    public Path store(MultipartFile file) throws MarcoException {
        try {
            String folderName = getFolderFromFileName(file.getOriginalFilename());
            Path fileFolder = this.root.resolve(folderName);
            Files.createDirectories(fileFolder);
            Path fileDestination = fileFolder.resolve(file.getOriginalFilename());
            
            _LOGGER.debug(String.format("Saving file: %s", fileDestination.toString()));
            Files.copy(file.getInputStream(), fileDestination, StandardCopyOption.REPLACE_EXISTING);
            _LOGGER.debug("File saved");
            
            EntityProcessQueue entity = new EntityProcessQueue();
            entity.setFileName(fileDestination.toFile().getAbsolutePath());
            entity.setProcessStatus(DemProcessStatus.NOT_PROCESSED);
            entity.setQueuedOn(LocalDateTime.now(ZoneOffset.UTC));
            repoQueue.saveEntity(entity);
            
            _LOGGER.debug("DB updates");
            return fileDestination;
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
        Map<String, List<FileInfo>> map = new TreeMap<>(Comparator.reverseOrder());

        List<File> files = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toFile().getName().endsWith(".dem")).map(Path::toFile).forEach(files::add);
        } catch (IOException e) {
            if (_LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            throw new MarcoException(e);
        }

        files.stream().forEach(f -> {
            String folderName = getFolderFromFileName(f.getName());
            map.compute(folderName, (k, v) -> {
                if (v == null) {
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
        String[] arr = fileName.split("-");
        return DateUtils.fromLocalDateToString(DateUtils.fromStringToLocalDate(arr[1], DateFormats.FOLDER_NAME),
                DateFormats.DB_DATE);
    }

    private String getMapFromFileName(String fileName) {
        String[] arr = fileName.split("-");
        return arr[4];
    }
}
