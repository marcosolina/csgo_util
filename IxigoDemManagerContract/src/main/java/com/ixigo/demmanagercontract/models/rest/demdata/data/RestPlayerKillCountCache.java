package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestPlayerKillCountCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long kill_count = null;
	private String victim = "";
	private String killer = "";

}
