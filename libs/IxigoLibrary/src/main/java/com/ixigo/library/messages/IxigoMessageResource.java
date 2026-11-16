package com.ixigo.library.messages;

/**
 * Interface used to retrieve Resource messages
 * 
 * @author Marco
 *
 */
public interface IxigoMessageResource {

    /**
     * It will return the message associated to the specific code
     * 
     * @param code
     * @param args
     * @return
     */
    public String getMessage(String code, String... args);
}
