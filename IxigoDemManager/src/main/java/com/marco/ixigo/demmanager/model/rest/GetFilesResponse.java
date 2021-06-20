package com.marco.ixigo.demmanager.model.rest;

import java.util.List;
import java.util.Map;

import com.marco.utils.http.MarcoResponse;

public class GetFilesResponse extends MarcoResponse {
    private Map<String, List<FileInfo>> files;

    public Map<String, List<FileInfo>> getFiles() {
        return files;
    }

    public void setFiles(Map<String, List<FileInfo>> files) {
        this.files = files;
    }
}
