package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestPlayerWeaponMapRankingCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private String weapon = "";
	private String mapname = "";
	private Long weapon_rank = null;

}
