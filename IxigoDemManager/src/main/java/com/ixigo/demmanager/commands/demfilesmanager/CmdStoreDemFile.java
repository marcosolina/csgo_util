package com.ixigo.demmanager.commands.demfilesmanager;

import org.springframework.web.multipart.MultipartFile;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class CmdStoreDemFile implements WebCommandRequest<Void>{
	private MultipartFile file;

	public CmdStoreDemFile(MultipartFile file) {
		super();
		this.file = file;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
