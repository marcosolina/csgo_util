package com.ixigo.demmanager.models.database;

import java.time.LocalDateTime;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Dem_process_queueDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private LocalDateTime queued_on = null;
	private String file_name = "";
	private DemProcessStatus process_status;
	private LocalDateTime processed_on = null;

}
