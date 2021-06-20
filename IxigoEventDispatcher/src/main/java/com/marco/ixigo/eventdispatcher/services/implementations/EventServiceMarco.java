package com.marco.ixigo.eventdispatcher.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.marco.ixigo.eventdispatcher.enums.EventType;
import com.marco.ixigo.eventdispatcher.model.entities.EntityEventListener;
import com.marco.ixigo.eventdispatcher.model.entities.EntityEventListenerPk;
import com.marco.ixigo.eventdispatcher.model.rest.DispatchedEventMessage;
import com.marco.ixigo.eventdispatcher.repositories.interfaces.RepoEntityEventListener;
import com.marco.ixigo.eventdispatcher.services.interfaces.EventService;
import com.marco.ixigo.eventdispatcher.services.interfaces.NotificationService;
import com.marco.utils.MarcoException;
import com.marco.utils.network.MarcoNetworkUtils;

public class EventServiceMarco implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceMarco.class);

    @Autowired
    private RepoEntityEventListener repo;
    @Autowired
    private MessageSource msgSource;
    @Autowired
    private NotificationService ns;
    @Autowired
    @Qualifier("NetworkUtilsNotBalanced")
    private MarcoNetworkUtils mnu;
    
    private static final String NEW_LINE_CHAR = "\n";
    
    private static final Map<String, EventType> previousEvent = new ConcurrentHashMap<>();

    @Override
    public void newIncomingEventFromServer(EventType event, String clientIp) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Processing event: %s", event.name()));
        }

        if (previousEvent.get(clientIp) == EventType.WARMUP_START && event == EventType.ROUND_START) {
            dispatchEvent(EventType.WARMUP_END);
        }

        previousEvent.put(clientIp, event);
        dispatchEvent(event);
    }

    private void dispatchEvent(EventType event) {
        DispatchedEventMessage lm = new DispatchedEventMessage();
        lm.setEventTime(LocalDateTime.now());
        lm.setEventType(event);

        List<EntityEventListener> listeners = repo.getListernersOfEvent(event);
        // @formatter:off
        listeners.parallelStream().forEach(l -> 
            /*
             * Don't stop the execution. Try to dispatch the event,
             * if successful good, otherwise mark the lister as not
             * active so I will not try again.
             */
            new Thread(() -> {
                try {
                    URL url = new URL(l.getId().getUrlListener());
                    ClientResponse resp = mnu.performPostRequest(url, Optional.empty(), Optional.of(lm));
                    LOGGER.debug(String.format("Call %s Status Code: %s", l.getId().getUrlListener(), resp.statusCode().name()));
                    if(resp.statusCode() != HttpStatus.OK) {
                        setFailure(l, String.format("Http status: %s", resp.statusCode().name()));
                    }else {
                        setSuccess(l);
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    setFailure(l, e.getMessage());
                }
            }).start()
        );
        // @formatter:on
    }

    private void setFailure(EntityEventListener l, String errorMessage) {
        l.setLastFailure(LocalDateTime.now());
        l.setConsecutiveFailure(l.getConsecutiveFailure() + 1);
        
        if (l.getConsecutiveFailure() > 2) {
            l.setActive("N");
        }
        try {
            repo.updateEntity(l);
            
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s- URL: %s", NEW_LINE_CHAR, l.getId().getUrlListener()));
            sb.append(String.format("%s- Event: %s", NEW_LINE_CHAR, l.getId().getEventType().name()));
            sb.append(String.format("%s- Attempt: %d", NEW_LINE_CHAR, l.getConsecutiveFailure()));
            sb.append(String.format("%s- Reason: %s", NEW_LINE_CHAR, errorMessage));
            
            sendNotifcation("Not able to dispatch the event", sb.toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void setSuccess(EntityEventListener l) {
        l.setLastSuccessful(LocalDateTime.now());
        l.setConsecutiveFailure(0);
        try {
            repo.updateEntity(l);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public boolean registerNewListener(String listenerUrl, EventType event) throws MarcoException {

        checkListenerKey(listenerUrl, event);

        EntityEventListener listener = new EntityEventListener();
        EntityEventListenerPk pk = new EntityEventListenerPk();

        listener.setId(pk);
        pk.setEventType(event);
        pk.setUrlListener(listenerUrl);
        listener.setActive("Y");
        listener.setConsecutiveFailure(0);

        if (repo.findListener(pk) != null) {
            throw new MarcoException(msgSource.getMessage("DISP00001", null, LocaleContextHolder.getLocale()));
        }
        
        boolean resp = repo.registerNewListener(listener);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s- URL: %s", NEW_LINE_CHAR, listener.getId().getUrlListener()));
        sb.append(String.format("%s- Event: %s", NEW_LINE_CHAR, listener.getId().getEventType().name()));
        sendNotifcation("New Event Listener registerd", sb.toString());
        
        return resp;
    }
    
    private void sendNotifcation(String title, String message) {
        try {
            ns.sendEventServiceError(title, message);
        }catch(Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public boolean deleteListener(String listenerUrl, EventType event) throws MarcoException {
        checkListenerKey(listenerUrl, event);
        EntityEventListenerPk pk = new EntityEventListenerPk();
        pk.setEventType(event);
        pk.setUrlListener(listenerUrl);
        boolean resp = repo.deleteListener(pk);
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s- URL: %s", NEW_LINE_CHAR, pk.getUrlListener()));
        sb.append(String.format("%s- Event: %s", NEW_LINE_CHAR, pk.getEventType().name()));
        sendNotifcation("Delete Event Listener", sb.toString());
        return resp;
    }

    private void checkListenerKey(String listenerUrl, EventType event) throws MarcoException {
        if (event == null) {
            throw new MarcoException(msgSource.getMessage("CSGOAPI00006", null, LocaleContextHolder.getLocale()));
        }

        try {
            new URL(listenerUrl);
        } catch (MalformedURLException e) {
            throw new MarcoException(msgSource.getMessage("CSGOAPI00007", null, LocaleContextHolder.getLocale()));
        }
    }
}