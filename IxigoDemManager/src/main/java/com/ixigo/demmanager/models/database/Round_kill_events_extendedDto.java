package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Round_kill_events_extendedDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String victimsteamid = "";
	private Boolean isfirstkill = null;
	private Long eventtime = null;
	private Boolean istradekill = null;
	private String assister = "";
	private String steamid = "";
	private String weapon = "";
	private String match_filename = "";
	private Long killer_team = null;
	private String flashassister = "";
	private Long round = null;
	private String killerflashed = "";
	private Boolean headshot = null;
	private Long victim_team = null;
	private Boolean istradedeath = null;

}
