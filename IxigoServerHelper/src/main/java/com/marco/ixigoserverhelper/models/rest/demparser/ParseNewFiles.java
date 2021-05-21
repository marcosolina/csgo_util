package com.marco.ixigoserverhelper.models.rest.demparser;

/**
 * Rest Model used to request the DEM service to parse the new DEM file/s
 * 
 * @author Marco
 *
 */
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
