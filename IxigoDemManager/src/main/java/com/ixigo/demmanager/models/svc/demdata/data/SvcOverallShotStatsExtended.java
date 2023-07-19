package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcOverallShotStatsExtended implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private String weapon = "";
	private Long shots_fired = null;

}
