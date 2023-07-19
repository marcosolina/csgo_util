package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestPlayerRoundAssistStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long round = null;
	private Long assists = null;
	private Long match_id = null;

}
