package com.ixigo.demmanager.models.entities;

import java.time.LocalDateTime;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.library.dao.IxigoDao;

public class DemProcessQueueDao extends IxigoDao {

	private static final long serialVersionUID = 1L;
	private DemProcessQueueDto dto = null;

	public DemProcessQueueDao() {
		this.setSqlViewName("dem_process_queue");
		this.setSqlKeys(new String[] { "file_name" });
		this.setSqlFields(new String[] { "queued_on", "file_name", "process_status", "processed_on" });
		this.dto = new DemProcessQueueDto();
	}

	public LocalDateTime getQueued_on() {
		return dto.getQueuedOn();
	}

	public void setQueued_on(LocalDateTime queued_on) {
		this.dto.setQueuedOn(queued_on);
	}

	public String getFile_name() {
		return dto.getFileName();
	}

	public void setFile_name(String file_name) {
		this.dto.setFileName(file_name);
	}

	public DemProcessStatus getProcess_status() {
		return dto.getProcessStatus();
	}

	public void setProcess_status(DemProcessStatus process_status) {
		this.dto.setProcessStatus(process_status);
	}

	public LocalDateTime getProcessed_on() {
		return dto.getProcessedOn();
	}

	public void setProcessed_on(LocalDateTime processed_on) {
		this.dto.setProcessedOn(processed_on);
	}

	public DemProcessQueueDto getDto() {
		return this.dto;
	}

	public void setDto(DemProcessQueueDto dto) {
		this.dto = dto;
	}

}
