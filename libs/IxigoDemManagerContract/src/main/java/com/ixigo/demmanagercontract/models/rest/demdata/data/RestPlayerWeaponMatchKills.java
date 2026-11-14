package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestPlayerWeaponMatchKills implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long kills = null;
	private String weapon = "";
	private Long headshots = null;
	private Long match_id = null;

}
