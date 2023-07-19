package com.ixigo.demmanager.models.svc.demdata.data;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
public class SvcPlayerRoundStats {
	private String userName;
    private String steamID;
    private Integer round;
    private Integer team;
    private BigDecimal clutchChance;
    private Boolean clutchSuccess;
    private Boolean survived;
    private Integer moneySpent;
    private Integer equipmentValue;
    private Boolean mvp;
    private Long matchId;
}
