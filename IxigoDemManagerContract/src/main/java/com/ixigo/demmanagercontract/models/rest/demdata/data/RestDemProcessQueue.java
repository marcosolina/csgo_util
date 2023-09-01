package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestDemProcessQueue implements Serializable {

	private static final long serialVersionUID = 1L;
	private LocalDateTime queued_on = null;
	private String file_name = "";
	private String process_status = "";
	private LocalDateTime processed_on = null;

}
