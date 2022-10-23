package com.ixigo.demmanager.models.rest.demdata;

import java.util.Map;

/**
 * Response model used to return the score type to use when
 * partition the teams
 * 
 * @author Marco
 *
 */
public class RestScoreTypes {
	private Map<String, String> types;

    public Map<String, String> getTypes() {
        return types;
    }

    public void setTypes(Map<String, String> types) {
        this.types = types;
    }
}
