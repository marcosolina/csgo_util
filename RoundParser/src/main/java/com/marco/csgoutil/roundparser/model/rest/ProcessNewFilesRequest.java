package com.marco.csgoutil.roundparser.model.rest;

public class ProcessNewFilesRequest {
    private boolean forceDeleteBadFiles;

    public boolean isForceDeleteBadFiles() {
        return forceDeleteBadFiles;
    }

    public void setForceDeleteBadFiles(boolean forceDeleteBadFiles) {
        this.forceDeleteBadFiles = forceDeleteBadFiles;
    }

}
