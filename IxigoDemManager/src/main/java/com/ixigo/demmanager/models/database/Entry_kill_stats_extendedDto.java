package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class Entry_kill_stats_extendedDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private Long total_rounds_t = null;
	private Long ekct_success = null;
	private Long ekt_attempts = null;
	private Long ekt_success = null;
	private Long ekct_attempts = null;
	private Long total_rounds = null;
	private BigDecimal ekct_success_rate_overall = BigDecimal.ZERO;
	private BigDecimal ekt_success_rate = BigDecimal.ZERO;
	private String steamid = "";
	private BigDecimal ek_success_rate_overall = BigDecimal.ZERO;
	private BigDecimal ek_success_rate = BigDecimal.ZERO;
	private Long total_rounds_ct = null;
	private Long ek_success = null;
	private BigDecimal ekct_success_rate = BigDecimal.ZERO;
	private Long ek_attempts = null;
	private BigDecimal ekt_success_rate_overall = BigDecimal.ZERO;

}
