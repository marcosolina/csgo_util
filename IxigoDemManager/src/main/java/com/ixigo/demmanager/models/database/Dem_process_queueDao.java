package com.ixigo.demmanager.models.database;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Dem_process_queueDao extends IxigoDao<Dem_process_queueDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Dem_process_queueDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "dem_process_queue";
	private Dem_process_queueDto dto = null;

	public Dem_process_queueDao() {
		_LOGGER.trace("Instanciating Dem_process_queueDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {
			Dem_process_queueDto.Fields.file_name,
		});
		this.setSqlAutoincrementalFiles(Arrays.asList(new String[] {
		}));
		this.setSqlFields(new String[] {
			Dem_process_queueDto.Fields.queued_on,
			Dem_process_queueDto.Fields.file_name,
			Dem_process_queueDto.Fields.process_status,
			Dem_process_queueDto.Fields.processed_on,
		});
		// @formatter:on
		this.dto = new Dem_process_queueDto();
	}

	@Override
	public Dem_process_queueDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		Dem_process_queueDto dto = new Dem_process_queueDto();
		dto.setFile_name(row.get(Dem_process_queueDto.Fields.file_name, String.class));
		dto.setProcessed_on(row.get(Dem_process_queueDto.Fields.processed_on, LocalDateTime.class));
		dto.setProcess_status(DemProcessStatus.valueOf(row.get(Dem_process_queueDto.Fields.process_status, String.class)));
		dto.setQueued_on(row.get(Dem_process_queueDto.Fields.queued_on, LocalDateTime.class));
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
