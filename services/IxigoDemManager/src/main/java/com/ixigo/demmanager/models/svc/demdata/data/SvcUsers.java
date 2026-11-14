package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcUsers implements Serializable {

	private static final long serialVersionUID = 1L;
	private String user_name = "";
	private String steam_id = "";

}
