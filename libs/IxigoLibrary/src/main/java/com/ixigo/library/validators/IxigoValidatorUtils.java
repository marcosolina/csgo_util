package com.ixigo.library.validators;

import java.util.Collection;
import java.util.stream.Collectors;

import com.ixigo.library.dto.ValidationError;

import br.com.fluentvalidator.context.Error;

public class IxigoValidatorUtils {
	public static Collection<ValidationError> fromCollectionErrorToValidationError(Collection<Error> errors) {
        if (errors == null) {
            return null;
        }

        return errors.stream().map(e -> {
            return new ValidationError(e.getField(), e.getMessage(), e.getCode(), e.getAttemptedValue());
        }).collect(Collectors.toList());
    }
}
