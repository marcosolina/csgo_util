package com.ixigo.demmanager.models.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.ixigo.demmanager.enums.DemProcessStatus;

@Table("DEM_PROCESS_QUEUE")
public class EntityProcessQueue implements Persistable<String> {

	@Id
	@Column("FILE_NAME")
	private String fileName;
	@Column("QUEUED_ON")
	private LocalDateTime queuedOn;
	@Column("PROCESSED_ON")
	private LocalDateTime processedOn;
	@Column("PROCESS_STATUS")
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

	@Override
	public String getId() {
		return this.fileName;
	}

	@Override
	public boolean isNew() {
		return this.fileName == null;
	}

}
