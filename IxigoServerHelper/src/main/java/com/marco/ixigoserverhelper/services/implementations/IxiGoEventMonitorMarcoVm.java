package com.marco.ixigoserverhelper.services.implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.marco.ixigoserverhelper.config.RconServiceProps;
import com.marco.ixigoserverhelper.models.rest.events.SendEvent;
import com.marco.ixigoserverhelper.services.interfaces.IxiGoEventMonitor;
import com.marco.utils.network.MarcoNetworkUtils;

@Component
@EnableScheduling
public class IxiGoEventMonitorMarcoVm implements IxiGoEventMonitor {
    private static final Logger _LOGGER = LoggerFactory.getLogger(IxiGoEventMonitorMarcoVm.class);
    
    @Autowired
    private RconServiceProps rconServiceProps;
    @Autowired
    @Qualifier("NonBalanced")
    private MarcoNetworkUtils mnu;
    private String oldValue = "NO";

    @Override
    @Scheduled(cron = "*/1 * * * * *")
    public String checkForNewEvent() {
        _LOGGER.trace("Checking event file");
        String currentValue = readFile();
        if(currentValue != null && !oldValue.equals(currentValue)) {
            oldValue = currentValue;
            try {
                SendEvent body = new SendEvent(oldValue);
                URL url = new URL(rconServiceProps.getProtocol(), rconServiceProps.getHost(), rconServiceProps.getPort(), rconServiceProps.getEventEndpoint());
                if(_LOGGER.isDebugEnabled()) {
                    _LOGGER.debug(String.format("Sending event: %s ", oldValue));
                }
                
                ClientResponse clientResp = mnu.performPostRequest(url, Optional.empty(), Optional.of(body));
                if(clientResp.statusCode() != HttpStatus.ACCEPTED && _LOGGER.isErrorEnabled()) {
                    _LOGGER.error(String.format("Not able to send the event: %s ", oldValue));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return oldValue;
    }
    
    private String readFile() {
        String cmd = String.format(
                "tail %s/csgo/addons/sourcemod/event.txt",
                System.getenv().get("ENV_CSGO_INSTALL_FOLDER"));
        _LOGGER.trace(cmd);

        try {
            String err = null;
            String input = null;
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder sbError = new StringBuilder();
            StringBuilder sbInput = new StringBuilder();
            
            while ((err = stdError.readLine()) != null) {
                sbError.append(err);
            }
            
            while ((input = stdInput.readLine()) != null) {
                sbInput.append(input);
            }
            
            if (sbError.length() == 0) {
                return sbInput.toString();
            }else {
                _LOGGER.error(sbError.toString());
            }
        } catch (IOException | InterruptedException e) {
            _LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
        
    }

}
