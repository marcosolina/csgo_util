package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
@Accessors(chain = true)
public class Round_kill_eventsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String victimsteamid = "";
	private Boolean isfirstkill = null;
	private BigDecimal eventtime = BigDecimal.ZERO;
	private Boolean istradekill = null;
	private String assister = "";
	private String steamid = "";
	private String weapon = "";
	private String match_filename = "";
	private String flashassister = "";
	private Long round = null;
	private String killerflashed = "";
	private Boolean headshot = null;
	private Boolean istradedeath = null;

}