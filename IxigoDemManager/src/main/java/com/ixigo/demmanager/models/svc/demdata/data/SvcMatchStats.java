package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcMatchStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private String match_filename = "";
	private Long match_id = null;
	private String mapname = "";
	private LocalDateTime match_date = null;

}
