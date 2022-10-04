package com.ixigo.library.validators;

import java.util.Collection;

import com.ixigo.library.dto.ValidationError;

/**
 * Exception that will contain the list of validation errors.
 * 
 * @author Marco
 *
 */
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final Collection<ValidationError> errors;

    public ValidationException(Collection<ValidationError> errors) {
        super();
        this.errors = errors;
    }

    public Collection<ValidationError> getErrors() {
        return errors;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
