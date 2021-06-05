package com.marco.csgoutil.roundparser.model.rest;

import java.util.List;

import com.marco.utils.http.MarcoResponse;

public class GetFiles extends MarcoResponse {
    private List<FileInfo> files;

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

}
