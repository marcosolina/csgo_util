package com.marco.ixigo.eventdispatcher;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marco.ixigo.eventdispatcher.enums.EventType;
import com.marco.ixigo.eventdispatcher.model.rest.EventListenerResgistraionRequest;
import com.marco.ixigo.eventdispatcher.model.rest.IncomingEventHttpRequest;
import com.marco.ixigo.eventdispatcher.model.rest.SimpleHttpResponseModel;
import com.marco.ixigo.eventdispatcher.services.interfaces.EventService;
import com.marco.utils.MarcoException;

import io.swagger.annotations.ApiOperation;

@RestController
public class EventsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    private EventService service;
    
    
    @PostMapping()
    @ApiOperation(value = "It receives the event from the CSGO server", code = 200)
    public ResponseEntity<Void> receiveServerEvent(@RequestBody IncomingEventHttpRequest request, HttpServletRequest httRequest) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("New event received: %s", request.toString()));
        }

        if (EventType.fromString(request.getEventName()) != null) {
            new Thread(() -> service.newIncomingEventFromServer(EventType.fromString(request.getEventName()), httRequest.getRemoteAddr())).start();
            LOGGER.trace("Leaving Events.receiveServerEvent");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    @ApiOperation(value = "End point to use if you want to register your event listener", code = 200)
    public ResponseEntity<SimpleHttpResponseModel> registerListener(@RequestBody EventListenerResgistraionRequest request) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Registering listener: %s - %s", request.getUrl(), request.getEventType()));
        }
        SimpleHttpResponseModel resp = new SimpleHttpResponseModel();
        try {
            resp.setStatus(service.registerNewListener(request.getUrl(), request.getEventType()));
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (MarcoException e) {
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/unregister")
    @ApiOperation(value = "End point to use if you want to delete your event listener", code = 200)
    public ResponseEntity<SimpleHttpResponseModel> unregisterListener(@RequestBody EventListenerResgistraionRequest request) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("UnRegistering listener: %s - %s", request.getUrl(), request.getEventType()));
        }
        SimpleHttpResponseModel resp = new SimpleHttpResponseModel();
        try {
            resp.setStatus(service.deleteListener(request.getUrl(), request.getEventType()));
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (MarcoException e) {
            resp.addError(e);
        }

        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
}
