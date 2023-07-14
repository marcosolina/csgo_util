package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcMapFileStats {
	private int matchId;
    private LocalDateTime matchDate;
    private String mapName;
    private String matchFileName;
}
