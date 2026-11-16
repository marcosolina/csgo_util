package com.ixigo.rconapi.config.filters;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.ixigo.library.filters.IxigoServiceExceptionHandler;
import com.ixigo.library.messages.IxigoMessageResource;

@ControllerAdvice
public class StandardIxigoErrors extends IxigoServiceExceptionHandler {

    public StandardIxigoErrors(IxigoMessageResource msgSource) {
        super(msgSource, "RCON00000");
    }

}