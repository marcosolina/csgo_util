package com.ixigo.demmanager.models.entities;

import java.time.LocalDateTime;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.library.dto.IxigoDto;

public class DemProcessQueueDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private LocalDateTime queuedOn = null;
	private String fileName = "";
	private DemProcessStatus processStatus;
	private LocalDateTime processedOn = null;

	public LocalDateTime getQueuedOn() {
		return queuedOn;
	}

	public void setQueuedOn(LocalDateTime queuedOn) {
		this.queuedOn = queuedOn;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public DemProcessStatus getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(DemProcessStatus processStatus) {
		this.processStatus = processStatus;
	}

	public LocalDateTime getProcessedOn() {
		return processedOn;
	}

	public void setProcessedOn(LocalDateTime processedOn) {
		this.processedOn = processedOn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
