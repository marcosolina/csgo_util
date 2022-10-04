package com.ixigo.library.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ixigo.library.dto.ValidationFailure;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.validators.ValidationException;



public abstract class IxigoServiceExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoServiceExceptionHandler.class);

    private IxigoMessageResource msgSource;
    private String interanlServerErroMsgKey;

    protected IxigoServiceExceptionHandler(IxigoMessageResource msgSource, String interanlServerErroMsgKey) {
        this.msgSource = msgSource;
        this.interanlServerErroMsgKey = interanlServerErroMsgKey;
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<String> hadleValidationException(Exception ex, WebRequest request) {
        ex.printStackTrace();

        return new ResponseEntity<>(msgSource.getMessage(interanlServerErroMsgKey), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { IxigoException.class })
    public ResponseEntity<String> hadleWeException(IxigoException ex, WebRequest request) {
        if (_LOGGER.isDebugEnabled()) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(value = { ValidationException.class })
    public ResponseEntity<ValidationFailure> hadleValidationException(ValidationException ex, WebRequest request) {
        if (_LOGGER.isDebugEnabled()) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ValidationFailure(ex.getErrors()), HttpStatus.BAD_REQUEST);
    }
}