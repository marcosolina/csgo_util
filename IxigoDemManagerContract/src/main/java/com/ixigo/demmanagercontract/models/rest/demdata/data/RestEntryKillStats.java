package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestEntryKillStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long total_rounds_t = null;
	private Long ekct_success = null;
	private Long total_rounds_ct = null;
	private Long ek_success = null;
	private Long ek_attempts = null;
	private Long ekt_attempts = null;
	private Long ekt_success = null;
	private Long ekct_attempts = null;
	private Long total_rounds = null;

}
