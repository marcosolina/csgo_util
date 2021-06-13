package com.marco.ixigo.demmanager.model.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.marco.ixigo.demmanager.enums.DemProcessStatus;

@Entity
@Table(name = "DEM_PROCESS_QUEUE")
public class EntityProcessQueue {
    @Id
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "QUEUED_ON")
    private LocalDateTime queuedOn;
    @Column(name = "PROCESSED_ON")
    private LocalDateTime processedOn;
    @Column(name = "PROCESS_STATUS")
    private DemProcessStatus processStatus;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getQueuedOn() {
        return queuedOn;
    }

    public void setQueuedOn(LocalDateTime queuedOn) {
        this.queuedOn = queuedOn;
    }

    public LocalDateTime getProcessedOn() {
        return processedOn;
    }

    public void setProcessedOn(LocalDateTime processedOn) {
        this.processedOn = processedOn;
    }

    public DemProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(DemProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

}
