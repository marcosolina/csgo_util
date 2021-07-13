package com.marco.ixigo.serverhelper.services.implementations;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.marco.ixigo.serverhelper.config.properties.AppProps;
import com.marco.ixigo.serverhelper.config.properties.DemManager;
import com.marco.ixigo.serverhelper.services.interfaces.DemFilesService;
import com.marco.utils.DateUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DateFormats;
import com.marco.utils.network.MarcoNetworkUtils;

public class DemFilesServiceMarco implements DemFilesService {
    private static final Logger _LOGGER = LoggerFactory.getLogger(DemFilesServiceMarco.class);
    
    @Autowired
    private AppProps appProps;
    @Autowired
    private DemManager demManagerProps;
    @Autowired
    private MarcoNetworkUtils mnu;
    @Autowired
    private WebClient.Builder wb;
    private static Map<String, String> filesSent = new HashMap<>();
    @Value("${com.marco.ixigo.serverhelper.justmondaynight:true}")
    private boolean justMondayNight = true;

    @Override
    public void sendLastDemFiles() throws MarcoException {

        if (justMondayNight && LocalDate.now().getDayOfWeek() != DayOfWeek.MONDAY) {
            _LOGGER.info("It is not the IxiGo night, no need to copy the dem files");
            return;
        }

        // Get all the files in the Server Folder
        _LOGGER.debug(String.format("Reading folder: %s", appProps.getDemFilesFolderFullPath()));
        File folder = new File(appProps.getDemFilesFolderFullPath());
        File[] files = folder.listFiles();

        // @formatter:off
        Arrays.stream(files)
        .filter(f -> f.getName().endsWith(".dem")) // Look for the "dem files"
        .filter(f -> { // Skip the small one (when nobody is playing)
            long bytes = f.length();
            long kb = bytes / 1024;
            long mb = kb / 1024;
            return mb > 2;
        })
        .filter(f -> !filesSent.containsKey(f.getAbsolutePath())) // Skip if the files is already managed
        .forEach(f -> filesSent.put(f.getAbsolutePath(), "N")); // Mark it to be sent

        List<String> fileNameList = filesSent.keySet().stream().collect(Collectors.toList());
        
        if(fileNameList.isEmpty()) {
            return;
        }
        
        /*
         * Sort the dem files by played date Descending
         */
        fileNameList.sort((o1, o2) -> {
            String[] tmp = o1.split("-");
            LocalDateTime ldt = DateUtils.fromStringToLocalDateTime(String.format("%s %s", tmp[1], tmp[2]), DateFormats.FILE_NAME_WITH_SPACE);
            String[] tmp2 = o2.split("-");
            LocalDateTime ldt2 = DateUtils.fromStringToLocalDateTime(String.format("%s %s", tmp2[1], tmp2[2]), DateFormats.FILE_NAME_WITH_SPACE);
            return ldt.compareTo(ldt2) * -1;
        });

        /*
         * Scp the files which are not sent yet
         */
        fileNameList.stream().filter(ldt -> {
            String tmp = filesSent.get(ldt);
            return !"Y".equals(tmp);
            }).forEach(this::postLastDemFile);
        // @formatter:on

        /*
         * Notify the DEM service that new dem files are available for processing
         */
        triggerParseNewDem();
    }

    private void triggerParseNewDem() {
        try {
            URL url = new URL(demManagerProps.getProtocol(), demManagerProps.getHost(), demManagerProps.getPort(), demManagerProps.getParseFile());
            _LOGGER.debug(String.format("Triggering the DEM parser at URL: %s", url.toString()));
            ClientResponse clientResp = mnu.performPostRequest(url, Optional.empty(), Optional.empty());
            if (clientResp.statusCode() != HttpStatus.OK) {
                _LOGGER.error("Not able to trigger the dem parsers api");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    

    private void postLastDemFile(String fileNameAbsolutePath){
        try {
            Path file = Paths.get(fileNameAbsolutePath);
            _LOGGER.debug(String.format("Sending dem file: %s", file.toUri().toString()));
            Resource resource = new UrlResource(file.toUri());

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", resource).filename(file.toFile().getName());

            MultiValueMap<String, HttpEntity<?>> parts = builder.build();

            URL url = new URL(demManagerProps.getProtocol(), demManagerProps.getHost(), demManagerProps.getPort(), demManagerProps.getUploadFile());
            // @formatter:off
            ClientResponse resp = wb.build().post().uri(uriBuilder -> {
                    UriBuilder ub = uriBuilder
                            .scheme(url.getProtocol())
                            .host(url.getHost())
                            .port(url.getPort())
                            .path(url.getPath());
                    return ub.build();
    
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(parts)
                .exchange()
                .block();
            // @formatter:on
            if(_LOGGER.isDebugEnabled()) {
                _LOGGER.debug(resp.statusCode().toString());
            }
            if (resp.statusCode() == HttpStatus.OK) {
                filesSent.remove(fileNameAbsolutePath);
                //filesSent.put(fileNameAbsolutePath, "Y");
            }
        } catch (IOException e) {
            _LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
