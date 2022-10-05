package com.ixigo.demmanager.models.rest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Marco
 *
 */
public class RestGetFilesResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("files")
	private Map<String, List<FileInfo>> files;

	public Map<String, List<FileInfo>> getFiles() {
		return files;
	}

	public void setFiles(Map<String, List<FileInfo>> files) {
		this.files = files;
	}
}
