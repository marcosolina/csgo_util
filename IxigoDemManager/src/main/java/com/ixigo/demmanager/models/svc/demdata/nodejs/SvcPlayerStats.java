package com.ixigo.demmanager.models.svc.demdata.nodejs;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
public class SvcPlayerStats {
	private String userName;
    private String steamID;
    private String fileName;
    private Integer score;
}
