package com.ixigo.demmanager.commands.charts;

import com.ixigo.demmanagercontract.models.rest.charts.RestViewData;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class CmdGetViewData implements WebCommandRequest<RestViewData> {
	private String viewName;

	public CmdGetViewData(String viewName) {
		super();
		this.viewName = viewName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}
