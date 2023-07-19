package com.ixigo.demmanager.models.svc.demdata.data;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
public class SvcMapStats {
	private LocalDateTime date;
	private String mapName;
	private String fileName;
	private Long matchId;
}
