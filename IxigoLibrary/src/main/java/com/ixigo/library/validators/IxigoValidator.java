package com.ixigo.library.validators;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.fluentvalidator.AbstractValidator;
/**
 * Ixigo extension of the Java fluent validator library
 * 
 * @author Marco
 * @see <a href="https://github.com/mvallim/java-fluent-validator">Java fluent
 *      validator</a>
 * @param <T>
 */
public abstract class IxigoValidator<T> extends AbstractValidator<T> {

    /**
     * It will try to get the value of the annotation @JsonProperty of the Java
     * field specified as input parameter.
     * 
     * @param fieldName
     * @return The JsonProperty value, or the same input value
     */
    public String getJsonPropertyName(String fieldName) {
        try {
            // @formatter:off
            Field f = (
                        (Class<?>)(
                                (ParameterizedType) this.getClass().getGenericSuperclass()
                                ).getActualTypeArguments()[0]
                       ).getDeclaredField(fieldName);
            // @formatter:on
            if (f == null) {
                return fieldName;
            }

            JsonProperty jp = f.getAnnotation(JsonProperty.class);
            if (jp != null) {
                return jp.value();
            }
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return fieldName;
    }
    
    public String getJsonPropertyNameFromClass(Class<?> className, String fieldName) {
        try {
            // @formatter:off
            Field f = className.getDeclaredField(fieldName);
            // @formatter:on
            if (f == null) {
                return fieldName;
            }

            JsonProperty jp = f.getAnnotation(JsonProperty.class);
            if (jp != null) {
                return jp.value();
            }
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return fieldName;
    }

    /**
     * Validate the UUID format
     * 
     * @param guid
     * @return true if it is a valid UUID, false otherwise
     */
    public static boolean isValidGuid(String guid) {
        try {
            UUID.fromString(guid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Validates the email format
     * 
     * @param email
     * @return true if it is a valid format, false otherwise
     */
    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        // @formatter:off
        /*
         * ^ # start-of-string 
         * (?=.*[0-9]) # a digit must occur at least once
         * (?=.*[a-z]) # a lower case letter must occur at least once 
         * (?=.*[A-Z]) # an upper case letter must occur at least once 
         * (?=.*[@#$%^&+=]) # a special character must occur at least once 
         * (?=\S+$) # no whitespace allowed in the entire string 
         * .{8,} # anything, at least eight places though 
         * $ # end-of-string
         */
        // @formatter:on

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(password).matches();
    }
}
