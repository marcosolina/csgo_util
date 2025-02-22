package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_round_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long round = null;
	private BigDecimal clutchchance = BigDecimal.ZERO;
	private Long moneyspent = null;
	private Boolean clutchsuccess = null;
	private Boolean mvp = null;
	private Long match_id = null;
	private Long equipmentvalue = null;
	private Long team = null;
	private Boolean survived = null;
	private String username = "";

}
