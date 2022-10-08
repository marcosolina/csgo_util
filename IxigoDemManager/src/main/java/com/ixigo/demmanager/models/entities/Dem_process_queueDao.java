package com.ixigo.demmanager.models.entities;

import java.time.LocalDateTime;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Dem_process_queueDao extends IxigoDao<Dem_process_queueDto> {

	private static final long serialVersionUID = 1L;
	private Dem_process_queueDto dto = null;

	public Dem_process_queueDao() {
		this.setSqlViewName("dem_process_queue");
		this.setSqlKeys(new String[] { "file_name" });
		this.setSqlFields(new String[] { "queued_on", "file_name", "process_status", "processed_on" });
		this.dto = new Dem_process_queueDto();
	}
	
	@Override
	public Dem_process_queueDto mappingFunction(Row row, RowMetadata rowMetaData) {
		Dem_process_queueDto dto = new Dem_process_queueDto();
		dto.setFile_name(row.get("FILE_NAME", String.class));
		dto.setProcessed_on(row.get("PROCESSED_ON", LocalDateTime.class));
		dto.setProcess_status(DemProcessStatus.valueOf(row.get("PROCESS_STATUS", String.class)));
		dto.setQueued_on(row.get("QUEUED_ON", LocalDateTime.class));
		return dto;
	}

	public LocalDateTime getQueued_on() {
		return dto.getQueued_on();
	}

	public void setQueued_on(LocalDateTime queued_on) {
		this.dto.setQueued_on(queued_on);
	}

	public String getFile_name() {
		return dto.getFile_name();
	}

	public void setFile_name(String file_name) {
		this.dto.setFile_name(file_name);
	}

	public DemProcessStatus getProcess_status() {
		return dto.getProcess_status();
	}

	public void setProcess_status(DemProcessStatus process_status) {
		this.dto.setProcess_status(process_status);
	}

	public LocalDateTime getProcessed_on() {
		return dto.getProcessed_on();
	}

	public void setProcessed_on(LocalDateTime processed_on) {
		this.dto.setProcessed_on(processed_on);
	}

	public Dem_process_queueDto getDto() {
		return this.dto;
	}

	public void setDto(Dem_process_queueDto dto) {
		this.dto = dto;
	}


}
