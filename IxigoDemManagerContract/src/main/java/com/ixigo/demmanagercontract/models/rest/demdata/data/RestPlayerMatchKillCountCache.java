package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestPlayerMatchKillCountCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long match_id = null;
	private Long kill_count = null;
	private String victim = "";
	private String killer = "";

}
