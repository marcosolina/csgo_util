package com.ixigo.demmanager.models.database;

import java.time.LocalDateTime;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.library.dto.IxigoDto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class Dem_process_queueDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private LocalDateTime queued_on = null;
	private String file_name = "";
	private DemProcessStatus process_status;
	private LocalDateTime processed_on = null;

	public LocalDateTime getQueued_on() {
		return this.queued_on;
	}

	public void setQueued_on(LocalDateTime queued_on) {
		this.queued_on = queued_on;
	}

	public String getFile_name() {
		return this.file_name;
	}

	public Dem_process_queueDto setFile_name(String file_name) {
		this.file_name = file_name;
		return this;
	}

	public DemProcessStatus getProcess_status() {
		return this.process_status;
	}

	public void setProcess_status(DemProcessStatus process_status) {
		this.process_status = process_status;
	}

	public LocalDateTime getProcessed_on() {
		return this.processed_on;
	}

	public void setProcessed_on(LocalDateTime processed_on) {
		this.processed_on = processed_on;
	}

}
