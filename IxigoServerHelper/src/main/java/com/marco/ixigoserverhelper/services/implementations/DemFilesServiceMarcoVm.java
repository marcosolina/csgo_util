package com.marco.ixigoserverhelper.services.implementations;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import com.marco.ixigoserverhelper.config.DemFilesServiceProps;
import com.marco.ixigoserverhelper.config.RoundParserServiceProp;
import com.marco.ixigoserverhelper.models.rest.demparser.ParseNewFiles;
import com.marco.ixigoserverhelper.services.interfaces.DemFilesService;
import com.marco.utils.DateUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DateFormats;
import com.marco.utils.network.MarcoNetworkUtils;

/**
 * The implementation that I run on my IxiGo VM
 * 
 * @author Marco
 *
 */
public class DemFilesServiceMarcoVm implements DemFilesService {
    private static final Logger _LOGGER = LoggerFactory.getLogger(DemFilesServiceMarcoVm.class);
    @Autowired
    private DemFilesServiceProps props;
    @Autowired
    private RoundParserServiceProp parserService;
    @Autowired
    private MarcoNetworkUtils mnu;
    @Autowired
    private WebClient.Builder wb;

    private static Map<String, String> filesSent = new HashMap<>();

    @Override
    public void sendLastDemFiles() throws MarcoException {

        /*
        if (LocalDate.now().getDayOfWeek() != DayOfWeek.MONDAY) {
            _LOGGER.info("It is not the IxiGo night, no need to copy the dem files");
            return;
        }
        */

        // Get all the files in the Server Folder
        File folder = new File(props.getDemFilesFolderFullPath());
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
        ParseNewFiles body = new ParseNewFiles(true);
        try {
            URL url = new URL(parserService.getProtocol(), parserService.getHost(), parserService.getEndpoint());
            ClientResponse clientResp = mnu.performPostRequest(url, Optional.empty(), Optional.of(body));
            if (clientResp.statusCode() != HttpStatus.OK) {
                _LOGGER.error("Not able to trigger the dem parsers api");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /*
    private String createRemoteDemDirectory(String fileName) {
        String[] tmp = fileName.split("-");
        String folderName = DateUtils.fromLocalDateToString(
                DateUtils.fromStringToLocalDate(tmp[1], DateFormats.FOLDER_NAME), DateFormats.DB_DATE);

        folderName = String.format("%s/demfiles/%s", System.getenv().get("ENV_SSH_FOLDER"), folderName);

        String cmd = String.format("ssh %s@%s mkdir -p %s", System.getenv().get("ENV_SSH_USER"),
                System.getenv().get("ENV_SSH_IP"), folderName);

        sendCmd(cmd);
        return folderName;
    }

    private void sendCmd(String cmd) {
        _LOGGER.debug(cmd);

        try {
            String err = null;
            _LOGGER.debug("Exectuting");
            Process p = Runtime.getRuntime().exec(cmd);
            _LOGGER.debug("Waiting");
            p.waitFor();
            _LOGGER.debug("Waited");
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            while ((err = stdError.readLine()) != null) {
                sb.append(err);
            }

            if (sb.length() != 0) {
                _LOGGER.error(sb.toString());
            }
        } catch (IOException | InterruptedException e) {
            _LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void performScpCommand(String fileName) {
        String folderName = createRemoteDemDirectory(fileName);
        String cmd = String.format("scp %s %s@%s:%s", fileName, System.getenv().get("ENV_SSH_USER"),
                System.getenv().get("ENV_SSH_IP"), folderName);
        _LOGGER.debug(cmd);

        try {
            String err = null;
            _LOGGER.debug("Exectuting");
            Process p = Runtime.getRuntime().exec(cmd);
            _LOGGER.debug("Waiting");
            p.waitFor();
            _LOGGER.debug("Waited");
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            while ((err = stdError.readLine()) != null) {
                sb.append(err);
            }

            if (sb.length() == 0) {
                filesSent.put(fileName, "Y");
            } else {
                _LOGGER.error(sb.toString());
            }
        } catch (IOException | InterruptedException e) {
            _LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

    }
    */

    private void postLastDemFile(String fileNameAbsolutePath){
        try {
            Path file = Paths.get(fileNameAbsolutePath);
            Resource resource = new UrlResource(file.toUri());

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", resource).filename(file.toFile().getName());

            MultiValueMap<String, HttpEntity<?>> parts = builder.build();

            URL url = new URL(parserService.getProtocol(), parserService.getHost(),
                    parserService.getDemFilesUploaEndpoint());
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

            _LOGGER.debug(resp.statusCode().toString());
            if (resp.statusCode() == HttpStatus.OK) {
                filesSent.put(fileNameAbsolutePath, "Y");
            }
        } catch (IOException e) {
            _LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
