package com.ixigo.demmanager.commands.demfilesmanager;

import org.springframework.core.io.Resource;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

/**
 * Command dispatched to retrieve (download) a DEM file
 * 
 * @author marco
 *
 */
public class CmdGetDemFile implements WebCommandRequest<Resource> {
	private String fileName;

	public CmdGetDemFile(String fileName) {
		super();
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
