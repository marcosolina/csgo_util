package com.ixigo.library.dto;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Simple DTO that will contain a list of validation errors.
 * 
 * @author Marco
 *
 */
public class ValidationFailure {
    
    @JsonProperty("errors")
    private Collection<ValidationError> errors;
    
    public ValidationFailure() {
        this.errors = new ArrayList<>();
    }
    
    public ValidationFailure(Collection<ValidationError> errors) {
        this.errors = errors;
    }

    public Collection<ValidationError> getErrors() {
        return errors;
    }
}
