package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
public class SvcMapFileStats {
	private LocalDateTime date;
	private String mapName;
	private String fileName;
}
