package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
@Accessors(chain = true)
public class Round_hit_eventsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private String weapon = "";
	private Long damagearmour = null;
	private String match_filename = "";
	private Long round = null;
	private String victimsteamid = "";
	private Long hitgroup = null;
	private Long damagehealth = null;
	private BigDecimal eventtime = BigDecimal.ZERO;
	private BigDecimal blindtime = BigDecimal.ZERO;

}