package com.marco.csgorestapi.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.util.UriBuilder;

import com.marco.csgorestapi.enums.EventType;
import com.marco.csgorestapi.model.entities.EntityEventListener;
import com.marco.csgorestapi.model.entities.EntityEventListenerPk;
import com.marco.csgorestapi.model.rest.ListenerMessage;
import com.marco.csgorestapi.repositories.interfaces.RepoEntityEventListener;
import com.marco.csgorestapi.services.interfaces.EventService;
import com.marco.utils.MarcoException;

/**
 * My implementation
 * 
 * @author Marco
 *
 */
public class EventServiceMarco implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceMarco.class);

    @Autowired
    private RepoEntityEventListener repo;

    @Autowired
    private MessageSource msgSource;

    @Autowired
    private WebClient.Builder wcb;

    @Override
    public void newIncomingEventFromServer(EventType event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Processing event: %s", event.name()));
        }

        ListenerMessage lm = new ListenerMessage();
        lm.setEventTime(LocalDateTime.now());
        lm.setEventType(event);

        List<EntityEventListener> listeners = repo.getListernersOfEvent(event);
        // @formatter:off
        listeners.parallelStream().forEach(l -> {
            /*
             * Don't stop the execution. Try to dispatch the event,
             * if successful good, otherwise mark the lister as not
             * active so I will not try again.
             */
            new Thread(() -> {
                try {
                    URL url = new URL(l.getId().getUrlListener());
                    ClientResponse resp = this.performRequest(HttpMethod.POST, url, null, null, MediaType.APPLICATION_JSON, lm);
                    LOGGER.debug(String.format("Call %s Status Code: %s", l.getId().getUrlListener(), resp.statusCode().name()));
                    if(resp.statusCode() != HttpStatus.OK) {
                        setFailure(l);
                    }else {
                        setSuccess(l);
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    setFailure(l);
                }
            }).start();
        });
        // @formatter:on

    }

    private void setFailure(EntityEventListener l) {
        l.setLastFailure(LocalDateTime.now());
        l.setConsecutiveFailure(l.getConsecutiveFailure() + 1);
        if (l.getConsecutiveFailure() > 2) {
            l.setActive("N");
        }
        try {
            repo.updateEntity(l);
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
            throw new MarcoException(msgSource.getMessage("CSGOAPI00008", null, LocaleContextHolder.getLocale()));
        }

        return repo.registerNewListener(listener);
    }

    @Override
    public boolean deleteListener(String listenerUrl, EventType event) throws MarcoException {
        checkListenerKey(listenerUrl, event);
        EntityEventListenerPk pk = new EntityEventListenerPk();
        pk.setEventType(event);
        pk.setUrlListener(listenerUrl);
        return repo.deleteListener(pk);
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

    private ClientResponse performRequest(HttpMethod method, URL url, Map<String, String> headers,
            Map<String, String> queryParameters, MediaType contentType, Object body) {

        /*
         * Create the request and adds query parameters if provided
         */
        RequestBodySpec rbs = wcb.build().method(method).uri(uriBuilder -> {
            UriBuilder ub = uriBuilder.scheme(url.getProtocol()).host(url.getHost()).port(url.getPort())
                    .path(url.getPath());
            if (queryParameters != null) {
                for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                    ub = ub.queryParam(entry.getKey(), entry.getValue());
                }
            }
            return ub.build();

        }).contentType(contentType);

        /*
         * Add HTTP headers if provided
         */
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                rbs = rbs.header(entry.getKey(), entry.getValue());
            }
        }

        /*
         * Perform the call
         */
        ClientResponse resp = null;
        if (body != null) {
            resp = rbs.bodyValue(body).exchange().block();
        } else {
            resp = rbs.exchange().block();
        }
        return resp;
    }
}