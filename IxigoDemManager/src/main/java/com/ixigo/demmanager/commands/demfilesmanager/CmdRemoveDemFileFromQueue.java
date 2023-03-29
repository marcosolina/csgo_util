package com.ixigo.demmanager.commands.demfilesmanager;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class CmdRemoveDemFileFromQueue implements WebCommandRequest<Void> {
	private String fileName;

	public CmdRemoveDemFileFromQueue(String fileName) {
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
