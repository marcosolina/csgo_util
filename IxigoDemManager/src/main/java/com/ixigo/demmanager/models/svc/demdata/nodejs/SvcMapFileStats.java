package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class SvcMapFileStats {
	private LocalDateTime date;
	private String mapName;
	private String fileName;
}
