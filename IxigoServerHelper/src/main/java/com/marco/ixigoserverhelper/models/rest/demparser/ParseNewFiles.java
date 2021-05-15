package com.marco.ixigoserverhelper.models.rest.demparser;

public class ParseNewFiles {
    private boolean forceDeleteBadFiles;
    
    public ParseNewFiles(boolean forceDeleteBadFiles) {
        this.forceDeleteBadFiles = forceDeleteBadFiles;
    }

    public boolean isForceDeleteBadFiles() {
        return forceDeleteBadFiles;
    }

    public void setForceDeleteBadFiles(boolean forceDeleteBadFiles) {
        this.forceDeleteBadFiles = forceDeleteBadFiles;
    }

}
