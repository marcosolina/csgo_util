package com.ixigo.demmanager.models.svc.demdata.nodejs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class SvcPlayerStats {
	private String userName;
    private String steamID;
    private String fileName;
    private Integer score;
}
