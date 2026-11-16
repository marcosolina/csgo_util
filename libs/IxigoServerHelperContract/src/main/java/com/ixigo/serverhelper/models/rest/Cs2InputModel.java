package com.ixigo.serverhelper.models.rest;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Model used to send inputs command to CS2
 * 
 * @author Marco
 *
 */
public class Cs2InputModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(notes = "The rcon command", required = true)
    private String cs2Input;


    public String getCs2Input() {
		return cs2Input;
	}


	public void setCs2Input(String cs2Input) {
		this.cs2Input = cs2Input;
	}


	public static long getSerialversionuid() {
        return serialVersionUID;
    }

	@Override
	public String toString() {
		return "Cs2InputModel [cs2Input=" + cs2Input + "]";
	}
}
