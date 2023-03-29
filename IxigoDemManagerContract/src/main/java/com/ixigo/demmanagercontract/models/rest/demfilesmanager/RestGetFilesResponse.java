package com.ixigo.demmanagercontract.models.rest.demfilesmanager;

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
	private Map<String, List<RestFileInfo>> files;

	public Map<String, List<RestFileInfo>> getFiles() {
		return files;
	}

	public void setFiles(Map<String, List<RestFileInfo>> files) {
		this.files = files;
	}
}
