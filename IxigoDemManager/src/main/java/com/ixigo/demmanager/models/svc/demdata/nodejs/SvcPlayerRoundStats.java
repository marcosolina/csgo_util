package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
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
    private String fileName;
}
