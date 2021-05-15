package com.marco.ixigoserverhelper.services.implementations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

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

    private static Map<String, String> filesSent = new HashMap<>();

    @Override
    public void scpLastDemFiles() throws MarcoException {
        // auto0-20210512-172448-378433005-workshop_523638720_fy_simpsons-IXI-GO__Monday_Nights

        File folder = new File(props.getDemFilesFolderFullPath());
        File[] files = folder.listFiles();
        Arrays.stream(files).filter(f -> f.getName().endsWith(".dem"))
        .filter(f -> {
            long bytes = f.length();
            long kb = bytes / 1024;
            long mb = kb / 1024;
            return mb > 2;
        })
        .filter(f -> !filesSent.containsKey(f.getAbsolutePath()))
        .forEach(f -> filesSent.put(f.getAbsolutePath(), "N"));

        List<String> fileNameList = filesSent.keySet().stream().collect(Collectors.toList());
        
        if(fileNameList.isEmpty()) {
            return;
        }
        
        fileNameList.sort((o1, o2) -> {
            String[] tmp = o1.split("-");
            LocalDateTime ldt = DateUtils.fromStringToLocalDateTime(String.format("%s %s", tmp[1], tmp[2]), DateFormats.FILE_NAME_WITH_SPACE);
            String[] tmp2 = o2.split("-");
            LocalDateTime ldt2 = DateUtils.fromStringToLocalDateTime(String.format("%s %s", tmp2[1], tmp2[2]), DateFormats.FILE_NAME_WITH_SPACE);
            return ldt.compareTo(ldt2) * -1;
        });

        fileNameList.stream().filter(ldt -> {
            String tmp = filesSent.get(ldt);
            return !"Y".equals(tmp);
            }).forEach(this::performScpCommand);
        triggerParseNewDem();
    }
    
    private void triggerParseNewDem() {
        ParseNewFiles body = new ParseNewFiles(true);
        try {
            URL url = new URL(parserService.getProtocol(), parserService.getHost(), parserService.getEndpoint());
            ClientResponse clientResp = mnu.performPostRequest(url, Optional.empty(), Optional.of(body));
            if(clientResp.statusCode() != HttpStatus.OK) {
                _LOGGER.error("Not able to trigger the dem parsers api");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    private String createRemoteDemDirectory(String fileName) {
        String[] tmp = fileName.split("-");
        String folderName = DateUtils.fromLocalDateToString(DateUtils.fromStringToLocalDate(tmp[1], DateFormats.FOLDER_NAME), DateFormats.DB_DATE);
        
        folderName = String.format("%s/demfiles/%s", System.getenv().get("ENV_SSH_FOLDER"), folderName);
        
        String cmd = String.format(
                "ssh %s@%s mkdir -p %s",
                System.getenv().get("ENV_SSH_USER"),
                System.getenv().get("ENV_SSH_IP"),
                folderName
                );
        
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
        String cmd = String.format(
                "scp %s %s@%s:%s", 
                fileName, 
                System.getenv().get("ENV_SSH_USER"), 
                System.getenv().get("ENV_SSH_IP"), 
                folderName);
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
            }else {
                _LOGGER.error(sb.toString());
            }
        } catch (IOException | InterruptedException e) {
            _LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

        
    }

}
